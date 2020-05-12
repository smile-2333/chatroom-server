package org.hj.chatroomserver.repository;

import org.hj.chatroomserver.model.entity.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat,Integer>, JpaSpecificationExecutor<PrivateChat> {
    List<PrivateChat>findBySenderId(int senderId);

    Optional<PrivateChat>findBySenderIdAndReceiverId(int senderId,int receiverId);
}
