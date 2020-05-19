package org.hj.chatroomserver.repository;

import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.enums.ContextType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>, JpaSpecificationExecutor<Message> {
    Page<Message> findBySenderIdAndContextType(int senderId, ContextType contextType, Pageable pageable);
}
