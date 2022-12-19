package hello.world.yun;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class HttpQueryObject {

    private String api;
    private String nodePath;
    private String orderBy;
    private int limitToFirst;
    private boolean firstLevel;

    public HttpQueryObject(String nodePath, int limitToFirst, boolean firstLevel) {
        this.api = "/get";
        this.nodePath = nodePath;
        this.orderBy = "$key";
        this.limitToFirst = limitToFirst;
        this.firstLevel = firstLevel;
    }

    public void setApi(String api) {
        this.api = api;
    }
    public String getApi() {
        return api;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }
    public String getNodePath() {
        return nodePath;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getOrderBy() {
        return orderBy;
    }

    public void setLimitToFirst(int limitToFirst) {
        this.limitToFirst = limitToFirst;
    }
    public int getLimitToFirst() {
        return limitToFirst;
    }

    public void setFirstLevel(boolean firstLevel) {
        this.firstLevel = firstLevel;
    }
    public boolean getFirstLevel() {
        return firstLevel;
    }
}
