package com.demo;

import static com.demo.util.ConstantIF.BUCKETNAME;
import static com.demo.util.ConstantIF.TEST_FILENAME;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.util.StorageFactory;
import com.google.api.services.storage.Storage;

@WebServlet(name = "BucketDataIODelete", value = "/delete")
public class BucketDataIODelete extends HttpServlet
{

    /**
     * Deletes an object in a bucket.
     *
     * @param path the path to the object to delete.
     * @param bucketName the bucket the object is contained in.
     */
    public static void deleteObject(String path, String bucketName)
        throws IOException, GeneralSecurityException
    {
        Storage client = StorageFactory.getService();
        client.objects().delete(bucketName, path).execute();
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        // Now delete the file
        try
        {
            deleteObject(TEST_FILENAME, BUCKETNAME);
            response.getWriter().println("<b> Deleted file : "+TEST_FILENAME+"  in bucket with success .</b>");
            
        }
        catch (GeneralSecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getWriter().println("<b> Unable to delete file error occoured </b>");
            
        }


    }
}
