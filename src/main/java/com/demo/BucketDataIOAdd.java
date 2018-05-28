package com.demo;

import static com.demo.util.ConstantIF.BUCKETNAME;
import static com.demo.util.ConstantIF.TEST_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.util.StorageFactory;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;

@WebServlet(name = "BucketDataIOAdd", value = "/add")
public class BucketDataIOAdd extends HttpServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -929264285635698907L;
 
    /**
     * Uploads data to an object in a bucket.
     *
     * @param name the name of the destination object.
     * @param contentType the MIME type of the data.
     * @param file the file to upload.
     * @param bucketName the name of the bucket to create the object in.
     */
    public static void uploadFile(
        String name, String contentType, File file, String bucketName)
        throws IOException, GeneralSecurityException
    {
        InputStreamContent contentStream = new InputStreamContent(
            contentType, new FileInputStream(file));
        // Setting the length improves upload performance
        contentStream.setLength(file.length());
        StorageObject objectMetadata = new StorageObject()
            // Set the destination object name
            .setName(name)
            // Set the access control list to publicly read-only
            .setAcl(Arrays.asList(
                new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        // Do the insert
        Storage client = StorageFactory.getService();
        Storage.Objects.Insert insertRequest = client.objects().insert(
            bucketName, objectMetadata, contentStream);

        insertRequest.execute();
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        // Create a temp file to upload
        Path tempPath = Files.createTempFile("StorageSample", "txt");
        Files.write(tempPath, "Sample file".getBytes());
        File tempFile = tempPath.toFile();
        tempFile.deleteOnExit();
        // Upload it
        try
        {
            uploadFile(TEST_FILENAME, "text/plain", tempFile, BUCKETNAME);
            response.getWriter().println("<b> Added file : "+TEST_FILENAME+"  in bucket with success .</b>");
        }
        catch (GeneralSecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getWriter().println("<b> Unable to add file error occoured </b>");
            
        }

    }

}
