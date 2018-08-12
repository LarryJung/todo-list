package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	@CreatedDate
	private LocalDateTime createdDate;

	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	public AbstractEntity(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractEntity that = (AbstractEntity) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(createdDate, that.createdDate) &&
				Objects.equals(modifiedDate, that.modifiedDate);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, createdDate, modifiedDate);
	}
}
