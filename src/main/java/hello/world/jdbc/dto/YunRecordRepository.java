package hello.world.jdbc.dto;

import hello.world.jdbc.entity.Book;
import hello.world.jdbc.entity.YunRecord;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.H2)
public interface YunRecordRepository extends CrudRepository<YunRecord,Long> {
    @Executable
    List<YunRecord> findByUrl(String url) ;

    @Executable
    List<YunRecord> findByKey(String key);



}
