package hello.world.jdbc.dto;

import hello.world.jdbc.entity.YunRecord;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import jakarta.persistence.Table;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
@Table(name = "YUN_RECORD")
public interface YunRecordRepository extends PageableRepository<YunRecord,Long> {
    @Executable
    List<YunRecord> findByUrl(@NotNull String url) ;

    Optional<YunRecord> findById(@NotNull Long id);

    Page<YunRecord> find(Pageable pageable);

    @Executable
    Page<YunRecord> findByUrlEqual(@NotNull String url, @NotNull Pageable pageable);

    @Executable
    List<YunRecord> findByKey(@NotNull String key);

    List<YunRecord> findByUrl(@NotNull String title ,@NotNull Pageable pageable);

//    @Query(value = "SELECT DISTINCT url FROM yun_record ;",nativeQuery = true)
    List<String> getDistinctUrl();

    @Executable
    void deleteByDateInBefore(@NotNull Date dateIn);

    @Executable
    Optional<YunRecord> findByUrlEqualAndKeyEqual(@NotNull String url,@NotNull String key);

    @Executable
    YunRecord update(YunRecord record);
}
