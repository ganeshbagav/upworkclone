package com.incubator.upwork.services;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class ClientService {

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private FreelancerRepository freelancerRepository;

        @Autowired
        private JobPostRepository jobPostRepository;

        @Autowired
        private ProjectRepository projectRepository;

        @Autowired
        private SkillTagsRepository skillTagsRepository;

        @Autowired
        private TransctionRepository transctionRepository;
        @Autowired
        private NotificationRepository notificationRepository;
        @Autowired
        private MileStoneRepository mileStoneRepository;

        public Boolean existsByEmail(String email) {
                return clientRepository.existsByEmail(email);
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

        Map<String, String> response = new WeakHashMap<String, String>();

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
                Client client = Client.builder().country(country).email(email).firstName(firstName).lastName(lastName)
                                .balance(1000)
                                .password(password).build();

                clientRepository.save(client);
                client = clientRepository.findByEmail(email);
                response.put("clientId", client.getClientId().toString());
                response.put("message", "user added");
                return ResponseEntity.ok().body(response);

        }

        public ResponseEntity<?> logIn(Map<String, String> entity) {
                response.clear();

                String email = entity.get("email");
                String password = entity.get("password");

                Client client = clientRepository.findByEmail(email);
                if (client == null) {
                        response.put("message", "Sign up first");
                        return ResponseEntity.ok().body(response);
                }

                if (client.getPassword().equals(password)) {
                        response.put("message", "logged in");
                        response.put("userType", "client");
                        response.put("clientId", client.getClientId().toString());
                        return ResponseEntity.ok().body(response);
                }
                response.put("message", "password incorrect");

                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> addJobPost(Map<String, T> entity) {
                response.clear();

                String jobTitle = (String) entity.get("jobTitle");
                String jobDescription = (String) entity.get("jobDescription");
                String category = (String) entity.get("jobCategory");
                List<String> skills = (List<String>) entity.get("jobSkills");
                String scope = (String) entity.get("jobScope");
                String budget = (String) entity.get("jobBudget");
                Integer clientId = Integer.parseInt((String) entity.get("clientId"));
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter formatterLocalTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                String time = formatterLocalTime.format(localTime);

                List<SkillTags> skillTags = new ArrayList<>();
                for (String part : skills) {
                        SkillTags skill = SkillTags.builder().skills(part).build();
                        skillTagsRepository.save(skill);
                        skillTags.add(skill);
                }
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String date = formatterLocalDate.format(localDate);

                System.out.println(skillTags);

                // int clientid = Integer.parseInt();
                JobPost jobPost = JobPost.builder().jobDescription(jobDescription).category(category)
                                .skillTags(skillTags)
                                .client(clientRepository.findById(clientId).get()).hide(0)
                                .scope(scope).budget(budget).time(Time.valueOf(time)).date(Date.valueOf(date))
                                .jobTitle(jobTitle)
                                .build();
                jobPostRepository.save(jobPost);
                for (SkillTags part : skillTags) {
                        part.setJobPost(jobPost);
                        skillTagsRepository.save(part);
                }
                // Map<String, String> response = new HashMap<String, String>();
                response.put("message", "Post Added");

                return ResponseEntity.ok().body(response);
        }

        public ResponseEntity<?> dashboard(Map<String, String> entity) {
                response.clear();

                String email = entity.get("email");
                // use to get client first name last name
                Client client = clientRepository.findByEmail(email);
                Integer jobPostCount = jobPostRepository.getJobPostCount(client.getClientId().toString(), 0);
                Integer completeProjectCount = projectRepository
                                .getClientCompleteProjectCount(client.getClientId().toString());
                Integer incompleteProjectCount = projectRepository
                                .getClientIncompleteProjectCount(client.getClientId().toString());

                // Add notification indicator
                response.put("firstName", client.getFirstName());
                response.put("lastName", client.getLastName());
                response.put("email", client.getEmail());
                response.put("clientId", client.getClientId().toString());
                response.put("jobPostCount", jobPostCount.toString());
                response.put("completeProjectCount", completeProjectCount.toString());
                response.put("incompleteProjectCount", incompleteProjectCount.toString());
                return ResponseEntity.ok().body(response);
        }

        /*
         * sends all the job post created by client in response sorted by date and time
         */
        public ResponseEntity<?> myJobPost(Map<String, String> entity) {
                response.clear();

                String email = entity.get("email");
                Client client = clientRepository.findByEmail(email);
                Sort sort = Sort.by(
                                Sort.Order.desc("date"),
                                Sort.Order.desc("time"));
                List<JobPost> jonJobPostList = jobPostRepository.findAllByClientAndHide(client, 0, sort);

                return ResponseEntity.ok().body(jonJobPostList);
        }

        public <T> ResponseEntity<?> getProjectsInfo(Map<String, String> entity) {
                response.clear();
                String email = entity.get("email");
                String status = entity.get("status");
                Client client = clientRepository.findByEmail(email);
                if (status.equals("complete")) {
                        List<Project> projects = projectRepository
                                        .getClientCompleteProject(client.getClientId().toString());
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
                                Freelancer freelancer = freelancerRepository.findById(project.getFreelancerid()).get();
                                responseProject.put("freelancerName",
                                                (T) (freelancer.getFirstName() + " " + freelancer.getLastName()));
                                List<MileStone> mileStones = mileStoneRepository.findAllByProject(project);
                                responseProject.put("millstones", (T) mileStones);
                                projectInfoList.add(responseProject);
                        }

                        return ResponseEntity.ok().body(projectInfoList);
                }
                List<Project> projects = projectRepository.getClientIncompleteProject(client.getClientId().toString());
                List<Map<String, T>> projectInfoList = new ArrayList<>();

                for (Project project : projects) {
                        Map<String, T> responseProject = new WeakHashMap<>();
                        responseProject.put("clientDeposit", (T) project.getClientdeposit());
                        responseProject.put("clientId", (T) project.getClientid());
                        responseProject.put("endDate", (T) project.getEndDate());
                        responseProject.put("startDate", (T) project.getStartDate());
                        responseProject.put("jobPostId", (T) project.getJobpostid());
                        responseProject.put("freelancerId", (T) project.getFreelancerid());
                        responseProject.put("totalBudget", (T) (project.getBudget() + ""));
                        responseProject.put("projectId", (T) (project.getProjectId() + ""));
                        responseProject.put("onGoingMileStone", (T) project.getOnGoingMileStone());
                        responseProject.put("title",
                                        (T) jobPostRepository.findById(project.getJobpostid()).get().getJobTitle());
                        Freelancer freelancer = freelancerRepository.findById(project.getFreelancerid()).get();
                        responseProject.put("freelancerName",
                                        (T) (freelancer.getFirstName() + " " + freelancer.getLastName()));
                        List<MileStone> mileStones = mileStoneRepository.findAllByProject(project);
                        responseProject.put("millstones", (T) mileStones);
                        projectInfoList.add(responseProject);
                }
                return ResponseEntity.ok()
                                .body(projectInfoList);
        }

        public <T> ResponseEntity<?> addBalance(Map<String, String> entity) {
                response.clear();
                float balance = Float.parseFloat(entity.get("balance"));
                String email = entity.get("email");
                if (clientRepository.existsByEmail(email)) {
                        Client client = clientRepository.findByEmail(email);
                        client.setBalance(balance + client.getBalance());
                        clientRepository.save(client);
                        response.put("message", "balance credited");
                        return ResponseEntity.ok().body(response);
                }

                response.put("message", "user not found");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> getJobPostProposal(Map<String, String> entity) {
                JobPost jobPost = jobPostRepository.findById(Integer.parseInt(entity.get("jobPostId"))).get();
                Iterator<JobProposals> iterator = jobPost.getJobProposals().iterator();
                List<Map<String, T>> responseProposalsList = new ArrayList<>();

                while (iterator.hasNext()) {
                        JobProposals jobProposals = iterator.next();

                        Freelancer freelancer = jobProposals.getFreelancer();
                        Map<String, T> freelancerProposal = new WeakHashMap<>();
                        freelancerProposal.put("firstName", (T) freelancer.getFirstName());
                        freelancerProposal.put("lastName", (T) freelancer.getLastName());
                        freelancerProposal.put("freelancerId", (T) freelancer.getFreelancerId());
                        freelancerProposal.put("jobProposalId", (T) (jobProposals.getJobproposalId() + ""));
                        List<SkillTags> skills = freelancer.getSkillTags();
                        List<String> skillset = new ArrayList<>();
                        for (SkillTags part : skills) {
                                skillset.add(part.getSkills());
                        }
                        freelancerProposal.put("skills", (T) skillset);
                        responseProposalsList.add(freelancerProposal);
                }
                return ResponseEntity.ok().body(responseProposalsList);
        }

        public <T> ResponseEntity<?> startProject(Map<String, T> entity) {
                response.clear();
                Integer freelancerid = Integer.parseInt((String) entity.get("freelancerid"));
                Integer jobpostid = Integer.parseInt((String) entity.get("jobpostid"));
                Integer jobproposalId = Integer.parseInt((String) entity.get("jobproposalid"));
                Integer clientId = Integer.parseInt((String) entity.get("clientid"));
                String startdate = (String) entity.get("startdate");
                String enddate = (String) entity.get("enddate");
                float budget = Float.parseFloat((String) entity.get("budget"));

                System.out.println(freelancerid);
                System.out.println(jobpostid);
                System.out.println(jobproposalId);
                System.out.println(clientId);
                System.out.println(startdate);
                System.out.println(enddate);
                System.out.println(budget);

                List<Map<String, T>> milestones = (List<Map<String, T>>) entity.get("milestone");
                // make job post to all freelancer
                JobPost jobPost = jobPostRepository.findById(jobpostid).get();
                jobPost.setHide(1);
                jobPostRepository.save(jobPost);

                JobProposals jobProposals = new JobProposals();
                List<MileStone> milestonesobj = new ArrayList<>();
                Iterator itr = milestones.iterator();
                int cnt = 0;
                Project project = Project.builder().startDate(Date.valueOf(startdate)).endDate(Date.valueOf(enddate))
                                .budget(budget).onGoingMileStone(1)
                                .clientid(clientId)
                                .jobpostid(jobpostid).freelancerid(freelancerid).iscomplete(0)
                                .clientdeposit(budget * (0.15f))
                                .freelancerdeposit(budget * (0.15f)).build();
                projectRepository.save(project);

                while (itr.hasNext()) {
                        Map<String, ? extends Comparable> m = (Map<String, ? extends Comparable>) itr.next();
                        String Title = (String) m.get("title");
                        String Description = (String) m.get("description");
                        // String productid =

                        // String mstartdate = (String) entity.get("mstartdate");

                        // String menddate = (String) entity.get("menddate");

                        // System.out.println("mstartdate " + mstartdate);
                        // System.out.println("menddate " + menddate);
                        System.out.println("Title " + Title);
                        System.out.println("Description " + Description);

                        Float payment = budget / milestones.size();
                        cnt++;
                        MileStone milestone = MileStone.builder().Description(Description).Title(Title)
                                        .milestonenum(cnt) // .startDate(Date.valueOf(mstartdate)).endDate(Date.valueOf(menddate))
                                        .payment(payment).project(project)
                                        .submission(0).verify(0).build();
                        milestonesobj.add(milestone);
                        mileStoneRepository.save(milestone);
                }

                Freelancer freelancer = freelancerRepository.findById(freelancerid).get();
                freelancer.setBalance(freelancer.getBalance() - budget * (0.15f));
                freelancerRepository.save(freelancer);

                Client client = clientRepository.findById(clientId).get();
                client.setBalance(client.getBalance() - budget * (0.15f));
                clientRepository.save(client);

                LocalTime localTime = LocalTime.now();
                DateTimeFormatter formatterLocalTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                String currenttime = formatterLocalTime.format(localTime);

                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String currentdate = formatterLocalDate.format(localDate);

                Transction transction1 = Transction.builder().time(Time.valueOf(currenttime))
                                .date(Date.valueOf(currentdate))
                                .from(new String(client.getFirstName() + " " + client.getLastName())).to("Admin")
                                .fromphone(client.getPhone()).tophone("1234567890")
                                .amount(budget * (0.15f)).projectid(project.getProjectId()).build();

                Transction transction2 = Transction.builder().time(Time.valueOf(currenttime))
                                .date(Date.valueOf(currentdate))
                                .from(new String(freelancer.getFirstName() + " " + freelancer.getLastName()))
                                .fromphone(freelancer.getPhone()).tophone("1234567890")
                                .to("Admin")
                                .amount(budget * (0.15f)).projectid(project.getProjectId()).build();
                transctionRepository.save(transction1);
                transctionRepository.save(transction2);
                Notification notificationclient1 = Notification.builder()
                                .notification(new String(
                                                "Your account debited by unit " + budget * (0.15f)
                                                                + " available balance is unit " + client.getBalance()))
                                .client(client).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient1);

                Notification notificationfreelancer2 = Notification.builder()
                                .notification(new String(
                                                "Your account debited  by unit " + budget * (0.15f)
                                                                + " available balance is unit "
                                                                + freelancer.getBalance()))
                                .freelancer(freelancerRepository.findById(freelancerid).get()).viewed(0)
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .build();
                notificationRepository.save(notificationfreelancer2);

                Notification notificationclient = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " Started sucessfully"))
                                .client(client).viewed(0).time(Time.valueOf(currenttime))
                                .date(Date.valueOf(currentdate)).build();
                notificationRepository.save(notificationclient);
                Notification notificationfreelancer = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " Started sucessfully"))
                                .freelancer(freelancer).viewed(0).time(Time.valueOf(currenttime))
                                .date(Date.valueOf(currentdate))
                                .build();
                notificationRepository.save(notificationfreelancer);
                response.put("message", "success");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> completeMilestone(Map<String, T> entity) {
                response.clear();
                Integer milestoneid = Integer.parseInt(entity.get("milestoneid").toString());
                Integer projectid = Integer.parseInt((String)entity.get("projectid"));
                Integer freelancerid = Integer.parseInt(entity.get("freelancerid").toString());
                Integer jobpostid = Integer.parseInt(entity.get("jobpostid").toString());
                Integer clientId = Integer.parseInt(entity.get("clientid").toString());

                MileStone mileStone = mileStoneRepository.findById(milestoneid).get();
                mileStone.setVerify(1);
                mileStoneRepository.save(mileStone);

                Project project = projectRepository.findById(projectid).get();
                project.setOnGoingMileStone(project.getOnGoingMileStone() + 1);
                projectRepository.save(project);

                Notification notificationfreelancer1 = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " milestone verified by "
                                                                + clientRepository.findById(clientId).get()
                                                                                .getFirstName())
                                                + " " + clientRepository.findById(clientId).get().getLastName())
                                .freelancer(freelancerRepository.findById(freelancerid).get()).viewed(0)
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .build();
                notificationRepository.save(notificationfreelancer1);

                Client client = clientRepository.findById(clientId).get();
                client.setBalance(client.getBalance() - mileStone.getPayment());
                clientRepository.save(client);

                Notification notificationclient1 = Notification.builder()
                                .notification(new String(
                                                "Your account debited by unit " + mileStone.getPayment()
                                                                + " available balance is unit " + client.getBalance()))
                                .client(client).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient1);

                Freelancer freelancer = freelancerRepository.findById(freelancerid).get();
                freelancer.setEarnings(freelancer.getEarnings() + mileStone.getPayment());
                freelancerRepository.save(freelancer);

                Notification notificationfreelancer2 = Notification.builder()
                                .notification(new String(
                                                "Your account credited by unit " + mileStone.getPayment()
                                                                + " available balance is unit "
                                                                + freelancer.getBalance()))
                                .freelancer(freelancerRepository.findById(freelancerid).get()).viewed(0)
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .build();
                notificationRepository.save(notificationfreelancer2);

                Transction transction1 = Transction.builder().time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .from(new String(client.getFirstName() + " " + client.getLastName()))
                                .to(new String(freelancer.getFirstName() + " " + freelancer.getLastName()))
                                .fromphone(client.getPhone()).tophone(freelancer.getPhone())
                                .amount(mileStone.getPayment()).projectid(projectid).build();
                transctionRepository.save(transction1);

                response.put("message", "successful");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> completeProject(Map<String, T> entity) {
                response.clear();
                Integer freelancerid = Integer.parseInt((String) entity.get("freelancerid"));
                Integer jobpostid = Integer.parseInt((String) entity.get("jobpostid"));
                Integer jobproposalId = Integer.parseInt((String) entity.get("jobproposalid"));
                Integer clientId = Integer.parseInt((String) entity.get("clientid"));
                Integer projectId = Integer.parseInt((String) entity.get("projectid"));

                Project project = projectRepository.findById(projectId).get();

                Freelancer freelancer = freelancerRepository.findById(freelancerid).get();
                freelancer.setBalance(freelancer.getBalance() + project.getFreelancerdeposit());
                freelancerRepository.save(freelancer);

                Client client = clientRepository.findById(clientId).get();
                client.setBalance(client.getBalance() + project.getClientdeposit());
                clientRepository.save(client);
                Transction transction1 = Transction.builder().time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .from("Admin").to(new String(client.getFirstName() + " " + client.getLastName()))
                                .fromphone("1234567890").tophone(client.getPhone())
                                .amount(project.getClientdeposit()).projectid(project.getProjectId()).build();

                Transction transction2 = Transction.builder().time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .from("Admin")
                                .to(new String(freelancer.getFirstName() + " " + freelancer.getLastName()))
                                .fromphone("1234567890")
                                .tophone(freelancer.getPhone())
                                .amount(project.getFreelancerdeposit()).projectid(project.getProjectId()).build();
                transctionRepository.save(transction1);
                transctionRepository.save(transction2);

                Notification notificationclient = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " Completed sucessfully"))
                                .client(client).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient);
                Notification notificationfreelancer = Notification.builder()
                                .notification(new String(
                                                "Project " + jobPostRepository.findById(jobpostid).get().getJobTitle()
                                                                + " Completed sucessfully"))
                                .freelancer(freelancer).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .build();
                notificationRepository.save(notificationfreelancer);

                Notification notificationclient1 = Notification.builder()
                                .notification(new String(
                                                "Your account credited by unit " + project.getClientdeposit()
                                                                + " available balance is unit " + client.getBalance()))
                                .client(client).viewed(0).time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate())).build();
                notificationRepository.save(notificationclient1);

                Notification notificationfreelancer2 = Notification.builder()
                                .notification(new String(
                                                "Your account credited by unit " + project.getFreelancerdeposit()
                                                                + " available balance is unit "
                                                                + freelancer.getBalance()))
                                .freelancer(freelancerRepository.findById(freelancerid).get()).viewed(0)
                                .time(Time.valueOf(getCurrentTime()))
                                .date(Date.valueOf(getCurrentDate()))
                                .build();
                notificationRepository.save(notificationfreelancer2);

                project.setClientdeposit(0.0f);
                project.setFreelancerdeposit(0.0f);
                project.setIscomplete(1);
                projectRepository.save(project);
                response.put("message", "Sucessful");
                return ResponseEntity.ok().body(response);
        }

        public <T> ResponseEntity<?> searchTalent(Map<String, T> entity) {

                return null;
        }

        public <T> ResponseEntity<?> getProjectMilestone(@RequestBody Map<String, T> entity) {
                Project project = projectRepository.findById(Integer.parseInt((String) entity.get("projectid"))).get();
                Map<String,T> response = new  WeakHashMap<String,T>();
                response.put("onGoingMilestone", (T)project.getOnGoingMileStone().toString());
                List<MileStone> mileStones = mileStoneRepository.findAllByProject(project);
                response.put("mileStones",(T) mileStones);

                return ResponseEntity.ok().body(response);
        }

}
