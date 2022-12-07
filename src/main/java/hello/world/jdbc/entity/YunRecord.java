package hello.world.jdbc.entity;


import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.sql.Date;

@Serdeable
@Entity
public class YunRecord {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 512)
    private String url;

    @Column(length = 255)
    private String key;

    @Column(length = 4096)
    private String value;

    @DateCreated
    private Date dateIn;

    @DateUpdated
    private Date dateUp;

    public YunRecord(String url,String key,String value){
        this.setUrl(url);
        this.setKey(key);
        this.setValue(value);
    }

    public YunRecord() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDateUp() {
        return dateUp;
    }

    public void setDateUp(Date dateUp) {
        this.dateUp = dateUp;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }
}
