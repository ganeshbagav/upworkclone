package com.incubator.upwork.services;

import java.sql.Date;
import org.springframework.data.domain.Sort;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.lang.Comparable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.incubator.upwork.data.model.Client;
import com.incubator.upwork.data.model.Freelancer;
import com.incubator.upwork.data.model.JobPost;
import com.incubator.upwork.data.model.JobProposals;
import com.incubator.upwork.data.model.MileStone;
import com.incubator.upwork.data.model.Notification;
import com.incubator.upwork.data.model.Project;
import com.incubator.upwork.data.model.SkillTags;
import com.incubator.upwork.data.model.Transction;
import com.incubator.upwork.data.repository.ClientRepository;
import com.incubator.upwork.data.repository.FreelancerRepository;
import com.incubator.upwork.data.repository.JobPostRepository;
import com.incubator.upwork.data.repository.JobProposalsRepository;
import com.incubator.upwork.data.repository.MileStoneRepository;
import com.incubator.upwork.data.repository.NotificationRepository;
import com.incubator.upwork.data.repository.ProjectRepository;
import com.incubator.upwork.data.repository.SkillTagsRepository;
import com.incubator.upwork.data.repository.TransctionRepository;

@Service
public class FreelancerService {

        @Autowired
        private FreelancerRepository freelancerRepository;
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private JobProposalsRepository jobProposalsRepository;
        @Autowired
        private JobPostRepository jobPostRepository;
        @Autowired
        private ProjectRepository projectRepository;
        @Autowired
        private TransctionRepository transctionRepository;
        @Autowired
        private NotificationRepository notificationRepository;
        @Autowired
        private MileStoneRepository mileStoneRepository;
        @Autowired
        private SkillTagsRepository skillTagsRepository;

        Map<String, String> response = new WeakHashMap<String, String>();

        public Boolean existsByEmail(String email) {
                return freelancerRepository.existsByEmail(email);
        }

        public String getCurrentTime() {
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter formatterLocalTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                String currenttime = formatterLocalTime.format(localTime);

                return currenttime;
        }

        public String getCurrentDate() {
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String currentdate = formatterLocalDate.format(localDate);
                return currentdate;
        }

        public ResponseEntity<?> signUp(Map<String, String> entity) {
                response.clear();
                String email = entity.get("email");

                if (freelancerRepository.existsByEmail(email) || clientRepository.existsByEmail(email)) {
                        response.put("message", "email already in use");
                        return ResponseEntity.ok().body(response);
                }

                String firstName = entity.get("firstName");
                String lastName = entity.get("lastName");
                String password = entity.get("password");
                String country = entity.get("country");
                Freelancer freelancer = Freelancer.builder().country(country).email(email).firstName(firstName)
                                .balance(1000)
                                .lastName(lastName)
                                .password(password).connect(100).build();

                freelancerRepository.save(freelancer);
                freelancer = freelancerRepository.findByEmail(email).get();
                response.put("freelancerId", freelancer.getFreelancerId().toString());
                response.put("message", "user added");
                return ResponseEntity.ok().body(response);

        }

        public ResponseEntity<?> dashboard(Map<String, String> entity) {
                response.clear();

                String email = entity.get("email");
                // use to get client first name last name
                Freelancer freelancer = freelancerRepository.findByEmail(email).get();
                Integer completeProjectCount = projectRepository
                                .getFreelancerCompleteProjectCount(freelancer.getFreelancerId().toString());
                Integer incompleteProjectCount = projectRepository
                                .getFreelancerIncompleteProjectCount(freelancer.getFreelancerId().toString());
                Integer jobProposalCount = jobProposalsRepository
                                .getJobPostProposalCount(freelancer.getFreelancerId().toString());
                Sort sort = Sort.by(
                                Sort.Order.desc("date"),
                                Sort.Order.desc("time"));
                List<JobPost> jobPosts = jobPostRepository.findAll(sort);
                Iterator<JobPost> itr = jobPosts.iterator();
                System.out.println(jobPosts.size());
                int allJobPostCount = 0;
                while (itr.hasNext()) {
                        JobPost jobPost = itr.next();
                        if (jobProposalsRepository.findByFreelancerAndJobpost(freelancer, jobPost).size() == 1) {
                                allJobPostCount++;
                        }
                }
                System.out.println(allJobPostCount);
                // Add notification indicator
                response.put("freelancerId", freelancer.getFreelancerId().toString());
                response.put("firstName", freelancer.getFirstName());
                response.put("lastName", freelancer.getLastName());
                response.put("email", freelancer.getEmail());
                response.put("completeProjectCount", completeProjectCount.toString());
                response.put("incompleteProjectCount", incompleteProjectCount.toString());
                response.put("jobProposalCount", jobProposalCount.toString());
                response.put("allPostCount", (jobPosts.size() - allJobPostCount) + "");

                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> addBalance(Map<String, String> entity) {
                response.clear();
                float balance = Float.parseFloat(entity.get("balance"));
                String email = entity.get("email");
                if (freelancerRepository.existsByEmail(email)) {
                        Freelancer freelancer = freelancerRepository.findByEmail(email).get();
                        freelancer.setBalance(balance + freelancer.getBalance());
                        freelancerRepository.save(freelancer);
                        response.put("message", "balance credited");
                        return ResponseEntity.ok().body(response);
                }

                response.put("message", "user not found");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> addConnects(Map<String, String> entity) {
                response.clear();
                int connects = Integer.parseInt(entity.get("connects"));
                String email = entity.get("email");
                if (freelancerRepository.existsByEmail(email)) {
                        Freelancer freelancer = freelancerRepository.findByEmail(email).get();
                        freelancer.setBalance(connects + freelancer.getConnect());
                        freelancerRepository.save(freelancer);
                        response.put("message", "connects credited");
                        return ResponseEntity.ok().body(response);
                }

                response.put("message", "user not found");
                return ResponseEntity.ok().body(response);
        }

        public ResponseEntity<?> logIn(Map<String, String> entity) {
                response.clear();
                String email = entity.get("email");
                String password = entity.get("password");

                Freelancer freelancer = freelancerRepository.findByEmail(email).get();
                if (freelancer == null) {
                        response.put("message", "Sign up first");
                        return ResponseEntity.ok().body(response);
                }

                if (freelancer.getPassword().equals(password)) {
                        response.put("message", "logged in");
                        response.put("userType", "freelancer");
                        response.put("freelancerId", freelancer.getFreelancerId().toString());

                        return ResponseEntity.ok().body(response);
                }
                response.put("message", "password incorrect");
                return ResponseEntity.ok().body(response);

        }

        public ResponseEntity<?> jobProposal(Map<String, String> entity) {
                response.clear();
                Integer freelancerid = Integer.parseInt(entity.get("freelancerid"));
                Freelancer freelancer = freelancerRepository.findById(freelancerid).get();

                Integer jobpostid = Integer.parseInt(entity.get("jobpostid"));
                JobPost jobPost = jobPostRepository.findById(jobpostid).get();

                JobProposals jobproposals = JobProposals.builder()
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).hired(0)
                                .freelancer(freelancer)
                                .jobpost(jobPostRepository.findById(jobpostid).get()).build();

                jobProposalsRepository.save(jobproposals);
                Client client = jobPostRepository.findById(jobpostid).get().getClient();
                Notification notificationclient = Notification.builder()
                                .notification(new String(
                                                jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " got a proposal from "
                                                                + freelancerRepository.findById(freelancerid).get()
                                                                                .getFirstName()
                                                                + " "
                                                                + freelancerRepository.findById(freelancerid).get()
                                                                                .getLastName()))
                                .client(client).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient);

                response.put("message", "Successful");
                return ResponseEntity.ok().body(response);
        }

        public static <T> void revlist(List<T> list) {
                // base condition when the list size is 0
                if (list.size() <= 1 || list == null)
                        return;

                T value = list.remove(0);

                // call the recursive function to reverse
                // the list after removing the first element
                revlist(list);

                // now after the rest of the list has been
                // reversed by the upper recursive call,
                // add the first value at the end
                list.add(value);
        }

        public ResponseEntity<?> searchJob(Map<String, List<String>> entity) {
                response.clear();
                List<String> skills = entity.get("skills");
                // Set<JobPost> retJobPost1 = new TreeSet<>();
                List<JobPost> retJobPost = new ArrayList<>();

                for (String skill : skills) {
                        List<SkillTags> list = skillTagsRepository.findBySkillsIgnoreCaseContaining(skill);
                        for (SkillTags part : list) {
                                if (part.getJobPost() == null)
                                        continue;
                                retJobPost.add(part.getJobPost());

                        }
                }
                // for (JobPost part : retJobPost) {
                // if (part.equals(null))
                // continue;
                // retJobPost1.add(part); //Error = Why(TreeSet)
                // }
                revlist(retJobPost);
                System.out.println(retJobPost);
                return ResponseEntity.ok().body(retJobPost);
        }

        public <T> ResponseEntity<?> submitMilestone(Map<String, T> entity) {
                response.clear();
                Integer milestoneid = Integer.parseInt((String) entity.get("milestoneid"));
                Integer freelancerid = Integer.parseInt((String) entity.get("freelancerid"));
                Integer jobpostid = Integer.parseInt((String) entity.get("jobpostid"));
                Integer clientId = Integer.parseInt((String) entity.get("clientid"));

                MileStone mileStone = mileStoneRepository.findById(milestoneid).get();
                mileStone.setSubmission(1);
                mileStoneRepository.save(mileStone);
                Notification notificationclient = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " milestone submitted sucessfully by "
                                                                + freelancerRepository.findById(freelancerid).get()
                                                                                .getFirstName()
                                                                + " "
                                                                + freelancerRepository.findById(freelancerid).get()
                                                                                .getLastName()))
                                .client(clientRepository.findById(clientId).get()).viewed(0)
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient);
                response.put("message", "successful");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> getAllProjects(Map<String, T> entity) {
                Integer clientid = Integer.parseInt((String) entity.get("clientid"));
                List<Project> projects = projectRepository.findAllByClientid(clientid);
                return ResponseEntity.ok().body(projects);
        }

        public <T> ResponseEntity<?> getCompletionProject(Map<String, T> entity) {
                response.clear();
                Integer projectId = Integer.parseInt((String) entity.get("projectid"));
                Project project = projectRepository.findById(projectId).get();
                List<MileStone> mileStones = project.getMileStones();
                int flag = 1;
                for (MileStone part : mileStones) {
                        if (part.getVerify() == 0) {
                                flag = 0;
                                break;
                        }
                }
                if (flag == 0) {
                        response.put("message", "false");
                } else {
                        response.put("message", "true");
                }
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> getProjectsInfo(Map<String, String> entity) {
                response.clear();
                String email = entity.get("email");
                String status = entity.get("status");
                Freelancer freelancer = freelancerRepository.findByEmail(email).get();
                if (status.equals("complete")) {
                        List<Project> projects = projectRepository
                                        .getFreelancerCompleteProject(freelancer.getFreelancerId().toString());
                        List<Map<String, T>> projectInfoList = new ArrayList<>();
                        for (Project project : projects) {
                                Map<String, T> responseProject = new WeakHashMap<>();
                                responseProject.put("clientDeposit", (T) project.getClientdeposit());
                                responseProject.put("clientId", (T) project.getClientid());
                                responseProject.put("endDate", (T) project.getEndDate());
                                responseProject.put("startDate", (T) project.getStartDate());
                                responseProject.put("jobPostId", (T) project.getJobpostid());
                                responseProject.put("freelancerId", (T) project.getFreelancerid());
                                responseProject.put("onGoingMileStone", (T) project.getOnGoingMileStone());

                                responseProject.put("totalBudget", (T) (project.getBudget() + ""));
                                responseProject.put("projectId", (T) (project.getProjectId() + ""));
                                responseProject.put("title", (T) jobPostRepository.findById(project.getJobpostid())
                                                .get().getJobTitle());
                                Client client = clientRepository.findById(project.getClientid()).get();
                                responseProject.put("clientName",
                                                (T) (client.getFirstName() + " " + client.getLastName()));
                                List<MileStone> mileStones = mileStoneRepository.findAllByProject(project);
                                responseProject.put("millstones", (T) mileStones);
                                projectInfoList.add(responseProject);
                        }
                        return ResponseEntity.ok().body(projectInfoList);
                }
                List<Project> projects = projectRepository.getFreelancerIncompleteProject(
                                freelancer.getFreelancerId().toString());
                List<Map<String, T>> projectInfoList = new ArrayList<>();
                for (Project project : projects) {
                        Map<String, T> responseProject = new WeakHashMap<>();
                        responseProject.put("clientDeposit", (T) project.getClientdeposit());
                        responseProject.put("clientId", (T) project.getClientid());
                        responseProject.put("endDate", (T) project.getEndDate());
                        responseProject.put("startDate", (T) project.getStartDate());
                        responseProject.put("jobPostId", (T) project.getJobpostid());
                        responseProject.put("freelancerId", (T) project.getFreelancerid());
                        responseProject.put("onGoingMileStone", (T) project.getOnGoingMileStone());
                        responseProject.put("totalBudget", (T) (project.getBudget() + ""));
                        responseProject.put("projectId", (T) (project.getProjectId() + ""));
                        responseProject.put("title", (T) jobPostRepository.findById(project.getJobpostid())
                                        .get().getJobTitle());
                        Client client = clientRepository.findById(project.getClientid()).get();
                        responseProject.put("clientName",
                                        (T) (client.getFirstName() + " " + client.getLastName()));
                        List<MileStone> mileStones = mileStoneRepository.findAllByProject(project);
                        responseProject.put("millstones", (T) mileStones);
                        projectInfoList.add(responseProject);
                }
                return ResponseEntity.ok().body(projectInfoList);
        }

        public <T> ResponseEntity<?> getFreelancerFeed(Map<String, String> entity) {
                response.clear();
                System.out.println("get F feed");
                Sort sort = Sort.by(
                                Sort.Order.desc("date"),
                                Sort.Order.desc("time"));
                List<Map<String, T>> responseJobPost = new ArrayList<>();
                List<JobPost> jobPosts = jobPostRepository.findAll();
                Iterator<JobPost> itr = jobPosts.iterator();
                Freelancer freelancer = freelancerRepository.findByEmail(entity.get("email")).get();

                System.out.println(entity.get("email"));
                while (itr.hasNext()) {
                        JobPost jobPost = itr.next();
                        System.out.println(jobPost.getJobId());
                        if (jobProposalsRepository.findByFreelancerAndJobpost(freelancer, jobPost).size() == 1) {
                                continue;
                        }
                        Map<String, T> post = new WeakHashMap<>();
                        post.put("jobPostId", (T) jobPost.getJobId().toString());
                        post.put("budget", (T) jobPost.getBudget());
                        post.put("category", (T) jobPost.getCategory());
                        post.put("date", (T) jobPost.getDate().toString());
                        post.put("jobDescription", (T) jobPost.getJobDescription());
                        post.put("jobTitle", (T) jobPost.getJobTitle());
                        post.put("scope", (T) jobPost.getScope());
                        post.put("time", (T) jobPost.getTime().toString());
                        // System.out.println(jobPost.getSkillTags());
                        List<SkillTags> skills = jobPost.getSkillTags();
                        List<String> skillset = new ArrayList<>();
                        for (SkillTags part : skills) {
                                skillset.add(part.getSkills());
                        }
                        post.put("skills", (T) skillset);
                        responseJobPost.add(post);
                }
                revlist(responseJobPost);
                System.out.println("-------------------------------------------");
                return ResponseEntity.ok().body(responseJobPost);
        }

        public <T> ResponseEntity<?> getAppliedJobPost(Map<String, String> entity) {
                Integer id = Integer.parseInt(entity.get("freelancerid"));
                List<JobProposals> jobProposalsList = jobProposalsRepository
                                .findAllByFreelancer(freelancerRepository.findById(id).get());
                Iterator<JobProposals> itr = jobProposalsList.iterator();
                List<Map<String, T>> responseJobPost = new ArrayList<>();

                while (itr.hasNext()) {
                        JobPost jobPost = itr.next().getJobpost();
                        System.out.println(jobPost.getJobTitle());
                        Map<String, T> post = new WeakHashMap<>();
                        post.put("budget", (T) jobPost.getBudget());
                        post.put("category", (T) jobPost.getCategory());
                        post.put("discription", (T) jobPost.getJobDescription());
                        post.put("titel", (T) jobPost.getJobTitle());
                        post.put("category", (T) jobPost.getCategory());
                        post.put("date", (T) jobPost.getDate());

                        List<SkillTags> skills = jobPost.getSkillTags();
                        List<String> skillset = new ArrayList<>();
                        for (SkillTags part : skills) {
                                skillset.add(part.getSkills());
                        }
                        post.put("skills", (T) skillset);
                        responseJobPost.add(post);
                }
                revlist(responseJobPost);

                return ResponseEntity.ok().body(responseJobPost);
        }

}
