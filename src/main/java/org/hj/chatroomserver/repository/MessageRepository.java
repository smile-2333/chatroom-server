package org.hj.chatroomserver.repository;

import org.hj.chatroomserver.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>, JpaSpecificationExecutor<Message> {

}
