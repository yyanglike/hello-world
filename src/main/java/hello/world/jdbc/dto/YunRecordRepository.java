package hello.world.jdbc.dto;

import hello.world.jdbc.entity.YunRecord;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface YunRecordRepository extends PageableRepository<YunRecord,Long> {
    @Executable
    List<YunRecord> findByUrl(String url) ;

    Optional<YunRecord> findById(@NotNull Long id);

    Page<YunRecord> find(Pageable pageable);

    @Executable
    Page<YunRecord> findByUrlEqual(String url, Pageable pageable);

    @Executable
    List<YunRecord> findByKey(String key);

    List<YunRecord> findByUrl(String title ,Pageable pageable);

    List<String> queryDistinctKeyByUrl();

    @Executable
    void deleteByDateInBefore(Date dateIn);

    @Executable
    Optional<YunRecord> findByUrlEqualAndKeyEqual(String url,String key);

    @Executable
    void updateById(Long id,String value);
}
