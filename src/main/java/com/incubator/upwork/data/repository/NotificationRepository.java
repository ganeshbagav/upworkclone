package com.incubator.upwork.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Client;
import com.incubator.upwork.data.model.Freelancer;
import com.incubator.upwork.data.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    public List<Notification> findAllByFreelancer(Freelancer freelancer);

    public List<Notification> findAllByClient(Client client);

}
