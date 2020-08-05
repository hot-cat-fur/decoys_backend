package pesko.orgasms.app.events;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Listeners {

    private final AmazonS3 s3;
    @Value(value = "${aws.bucket}")
    private String bucketName;


    @Value(value = "${aws.file.path}")
    private String filePath;

    @Autowired
    public Listeners(AmazonS3 s3) {
        this.s3 = s3;
    }

    @EventListener(OrgasmDeletionEvent.class)
    public void orgasmDeletionLister(OrgasmDeletionEvent orgasmDeletionEvent){
       String keyTitle= orgasmDeletionEvent.getObjectName();
        s3.deleteObject(new DeleteObjectRequest(bucketName,filePath+keyTitle));
    }
}
