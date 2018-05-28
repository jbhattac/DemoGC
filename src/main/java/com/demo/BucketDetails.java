package com.demo;

import static com.demo.util.ConstantIF.BUCKETNAME;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.util.StorageFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

@WebServlet(name = "BucketDetails", value = "/bucket")
public class BucketDetails extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {

        StringBuilder builder = new StringBuilder();
        Bucket bucket = null;
        builder.append("<h3>Bucket Details </h3>");
        try
        {
            bucket = getBucket(BUCKETNAME);
            if (null != bucket)
            {
                builder.append("<b>name: </b>" + bucket.getName() + "</br>");
                builder.append("<b>location: </b>" + bucket.getLocation() + "</br>");
                builder.append("<b>timeCreated: </b>" + bucket.getTimeCreated() + "</br>");
                builder.append("<b>Full bucket details :</b>" + bucket.toPrettyString());

            }
        }
        catch (IOException | GeneralSecurityException e)
        {
            // TODO add proper exception handling
            e.printStackTrace();
        }

        // List the contents of the bucket.
        List<StorageObject> bucketContents = null;
        builder.append("<h3>Bucket Contents</h3>");
        try
        {
            bucketContents = listBucket(BUCKETNAME);
        }
        catch (IOException | GeneralSecurityException e)
        {
            // TODO add proper exception handling
            e.printStackTrace();
        }
        if (null == bucketContents || bucketContents.size() == 0)
        {
            builder.append(
                "There were no objects in the given bucket; try adding some and re-running.</br>");
        }
        else
        {
            for (StorageObject object : bucketContents)
            {
                builder.append(object.getName() + " (" + object.getSize() + " bytes)</br>");
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(builder.toString());
    }


    /**
     * Fetch a list of the objects within the given bucket.
     *
     * @param bucketName the name of the bucket to list.
     * @return a list of the contents of the specified bucket.
     */
    public static List<StorageObject> listBucket(String bucketName)
        throws IOException, GeneralSecurityException
    {
        Storage client = StorageFactory.getService();
        Storage.Objects.List listRequest = client.objects().list(bucketName);

        List<StorageObject> results = new ArrayList<StorageObject>();

        com.google.api.services.storage.model.Objects objects;
        // Iterate through each page of results, and add them to our results list.
        do
        {
            objects = listRequest.execute();

            if (null != objects && null != objects.getItems())
            {
                // Add the items in this page of results to the list we'll return.
                results.addAll(objects.getItems());

                // Get the next page, in the next iteration of this loop.
                listRequest.setPageToken(objects.getNextPageToken());
            }

        }
        while (null != objects.getNextPageToken());

        return results;
    }


    /**
     * Fetches the metadata for the given bucket.
     *
     * @param bucketName the name of the bucket to get metadata about.
     * @return a Bucket containing the bucket's metadata.
     */
    public static Bucket getBucket(String bucketName) throws IOException, GeneralSecurityException
    {
        Storage client = StorageFactory.getService();

        Storage.Buckets.Get bucketRequest = client.buckets().get(bucketName);
        // Fetch the full set of the bucket's properties 
        bucketRequest.setProjection("full");
        return bucketRequest.execute();
    }
}