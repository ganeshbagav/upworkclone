package com.incubator.upwork.services;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incubator.upwork.data.model.Category;
import com.incubator.upwork.data.model.Client;
import com.incubator.upwork.data.model.Notification;
import com.incubator.upwork.data.model.SkillTags;
import com.incubator.upwork.data.model.Transction;
import com.incubator.upwork.data.repository.CategoryRepository;
import com.incubator.upwork.data.repository.ClientRepository;
import com.incubator.upwork.data.repository.FreelancerRepository;
import com.incubator.upwork.data.repository.NotificationRepository;
import com.incubator.upwork.data.repository.SkillTagsRepository;
import com.incubator.upwork.data.repository.TransctionRepository;

@Service
public class GenericService {

    @Autowired
    private SkillTagsRepository skillTagsRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FreelancerRepository freelancerRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransctionRepository transactionRepository;

    static Map<String, String> response = new WeakHashMap<>();

    public ResponseEntity<?> Notification(Map<String, String> entity) {
        Integer id = 0;
        List<Notification> notifications = null;
        if (entity.get("clientid").equals(null)) {
            id = Integer.parseInt(entity.get("freelancerid"));
            notifications = notificationRepository.findAllByFreelancer(freelancerRepository.findById(id).get());
        } else {
            id = Integer.parseInt(entity.get("clientid"));
            notifications = notificationRepository.findAllByClient(clientRepository.findById(id).get());
        }
        return ResponseEntity.ok().body(notifications);
    }

    public ResponseEntity<?> viewedNotification(Map<String, String> entity) {
        response.clear();
        int id = Integer.parseInt(entity.get("notificationid"));
        Notification notification = notificationRepository.findById(id).get();
        notification.setViewed(1);
        notificationRepository.save(notification);
        response.put("message", "successful");
        return ResponseEntity.ok().body(response);
    }

    public <T> ResponseEntity<?> transaction(Map<String, T> entity) {

        String phone = (String) entity.get("phone");
        // private int transctionId;
        // private Time time;
        // private Date date;
        // private String from;
        // private String fromphone;
        // private String to;
        // private String tophone;
        // private float amount;
        // private int projectid;
        List<Transction> transctions = transactionRepository.findAllByPhoneOrderByTimeAndDate(phone);
        List<Map<String, T>> ret = new ArrayList<>();
        for (Transction part : transctions) {
            Map<String, T> m = new WeakHashMap<>();
            m.put("time", (T) part.getTime());
            m.put("date", (T) part.getDate());
            m.put("from", (T) part.getFrom());
            m.put("to", (T) part.getTo());
            m.put("amount",(T)Float.valueOf(part.getAmount()));
        }
        return ResponseEntity.ok().body(ret);
    }

}
