package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.entity.Comment;
import com.bestbenefits.takoyaki.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByParty(Party party);
}
