package pesko.orgasms.app.configurations;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class S3Configuration {


    @Value(value = "${aws.acc.key}")
   private String accesKey;
   @Value(value = "${aws.secret.key}")
   private String secretKey;

    @Bean
    public AmazonS3 s3(){

        AWSCredentials credentials= new BasicAWSCredentials
                        (accesKey
                        ,secretKey);

        AmazonS3 s3C1= AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("eu-central-1")
                .build();

        return s3C1;
    }

    public String getAccesKey() {
        return accesKey;
    }

    public void setAccesKey(String accesKey) {
        this.accesKey = accesKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
