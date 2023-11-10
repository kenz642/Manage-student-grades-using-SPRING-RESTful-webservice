package jmaster.io.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class TimeAuditable {

	@CreatedDate // auto gen new date
	@Column(updatable = false) // khi update cot nay khong thay doi
	private Date createdAt;
	
	private Date updatedAt;
}
