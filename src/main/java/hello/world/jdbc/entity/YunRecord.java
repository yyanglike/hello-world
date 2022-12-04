package hello.world.jdbc.entity;


import io.micronaut.context.annotation.Type;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;

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

    public YunRecord(String url,String key , String value){
        this.url = url;
        this.key = key;
        this.value = value;
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
}
