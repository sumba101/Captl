package one.captl.RESTful.notification.service;

import one.captl.RESTful.notification.dao.GpTasksRepository;
import one.captl.RESTful.notification.dao.LpTasksRepository;
import one.captl.RESTful.notification.dao.PortfolioTasksRepository;
import one.captl.RESTful.notification.model.classes.Frequencies;
import one.captl.RESTful.notification.model.classes.GpTasks;
import one.captl.RESTful.notification.model.classes.LpTasks;
import one.captl.RESTful.notification.model.classes.PortfolioTasks;
import one.captl.RESTful.notification.model.enums.AllActivities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CriticalService {

    private final GpTasksRepository gpTasksRepo;
    private final LpTasksRepository lpTasksRepo;
    private final PortfolioTasksRepository portfolioTasksRepo;

    @Value("${DaysBeforeForShowingReminder}")
    long reminderStart;

    @Value("${DaysBeforeForShowingDeadline}")
    long deadlineStart;

    public CriticalService(@Qualifier("gptasks") GpTasksRepository gpTasksRepo, @Qualifier("lptasks") LpTasksRepository lpTasksRepo, @Qualifier("portfoliotasks") PortfolioTasksRepository portfolioTasksRepo) {
        this.gpTasksRepo = gpTasksRepo;
        this.lpTasksRepo = lpTasksRepo;
        this.portfolioTasksRepo = portfolioTasksRepo;
    }


    public void starTask(int gplpOrportfolio, Long id, AllActivities allActivities) {
        if (gplpOrportfolio == 1) {
            if (gpTasksRepo.findById(id).isEmpty()) {
                throw new RuntimeException("GP tasks not found");
            }
            GpTasks gpTasks = gpTasksRepo.findById(id).get();
            List<AllActivities> starredWork = gpTasks.getStarredWork();
            if (starredWork == null) {
                starredWork=List.of(allActivities);
            }else{
                if (starredWork.contains(allActivities))
                    starredWork.remove(allActivities);
                else
                    starredWork.add(allActivities);
            }
            gpTasks.setStarredWork(starredWork);
            gpTasksRepo.save(gpTasks);
        }
        else if(gplpOrportfolio==2){
            if(lpTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("LP tasks not found");
            }
            LpTasks lpTasks=lpTasksRepo.findById(id).get();
            List<AllActivities> starredWork=lpTasks.getStarredWork();
            if(starredWork==null){
                starredWork=List.of(allActivities);
            }else {
                if (starredWork.contains(allActivities))
                    starredWork.remove(allActivities);
                else
                    starredWork.add(allActivities);
            }
            lpTasks.setStarredWork(starredWork);
            lpTasksRepo.save(lpTasks);

        }
        else if(gplpOrportfolio==3){
            if(portfolioTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("Portfolio tasks not found");
            }
            PortfolioTasks portfolioTasks=portfolioTasksRepo.findById(id).get();
            List<AllActivities> starredWork=portfolioTasks.getStarredWork();
            if(starredWork==null){
                starredWork=List.of(allActivities);
            }else {
                if (starredWork.contains(allActivities))
                    starredWork.remove(allActivities);
                else
                    starredWork.add(allActivities);
            }
            portfolioTasks.setStarredWork(starredWork);
            portfolioTasksRepo.save(portfolioTasks);
        }
    }

    public List<AllActivities> getStarredTasks(int gplpOrportfolio, Long id){
        if(gplpOrportfolio==1){
            if(gpTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("GP tasks not found");
            }
            GpTasks gpTasks=gpTasksRepo.findById(id).get();
            return gpTasks.getStarredWork();
        }
        else if(gplpOrportfolio==2){
            if(lpTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("LP tasks not found");
            }
            LpTasks lpTasks=lpTasksRepo.findById(id).get();
            return lpTasks.getStarredWork();

        }
        else if(gplpOrportfolio==3){
            if(portfolioTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("Portfolio tasks not found");
            }
            return portfolioTasksRepo.findById(id).get().getStarredWork();

        }
        else{
            throw new RuntimeException("Invalid value for Gp Lp or Portfolio");
        }

    }


    public void updateGpDeadline(long vintageId, AllActivities allActivities, Frequencies deadline){
        if(gpTasksRepo.findById(vintageId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(vintageId).get();
        HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
        if(deadlines==null){
            deadlines=new HashMap<>();
        }
        deadlines.put(allActivities,deadline);
        gpTasks.setDeadlines(deadlines);
        gpTasksRepo.save(gpTasks);
    }

    public void updateLpDeadline(long lpRoundId, AllActivities allActivities, Frequencies deadline){
        if(lpTasksRepo.findById(lpRoundId).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(lpRoundId).get();
        HashMap<AllActivities, Frequencies> deadlines=lpTasks.getDeadlines();
        if(deadlines==null){
            deadlines=new HashMap<>();
        }

        deadlines.put(allActivities,deadline);
        lpTasks.setDeadlines(deadlines);
        lpTasksRepo.save(lpTasks);
    }

    public void updatePortfolioDeadline(long pid, Long vinId, AllActivities allActivities, Frequencies deadline) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines = portfolioTasks.getDeadlines();
        if (deadlines == null) {
            deadlines = new HashMap<>();
        }
        HashMap<AllActivities, Frequencies> tempMap = new HashMap<>();
        tempMap.put(allActivities, deadline);
        deadlines.put(vinId, tempMap);
        portfolioTasks.setDeadlines(deadlines);
        portfolioTasksRepo.save(portfolioTasks);
    }

    ///////////////////////////////////////////////////////

    public void updateGpReminder(long userid, AllActivities allActivities, Frequencies reminder){
        if(gpTasksRepo.findById(userid).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(userid).get();
        HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();
        if(reminders==null){
            reminders=new HashMap<>();
        }

        reminders.put(allActivities,reminder);

        gpTasks.setReminders(reminders);
        gpTasksRepo.save(gpTasks);
    }

    public void updateLpReminder(long lpRoundId, AllActivities allActivities, Frequencies reminder){
        if(lpTasksRepo.findById(lpRoundId).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(lpRoundId).get();
        HashMap<AllActivities, Frequencies> reminders=lpTasks.getReminders();
        if(reminders==null){
            reminders=new HashMap<>();
        }

        reminders.put(allActivities,reminder);
        lpTasks.setReminders(reminders);
        lpTasksRepo.save(lpTasks);
    }


    public void updatePortfolioReminder(long id, Long vinId, AllActivities allActivities, Frequencies reminder) {
        if (portfolioTasksRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(id).get();

        HashMap<Long, HashMap<AllActivities, Frequencies>> reminders = portfolioTasks.getReminders();
        if (reminders == null) {
            reminders = new HashMap<>();
        }
        HashMap<AllActivities, Frequencies> tempMap = new HashMap<>();
        tempMap.put(allActivities, reminder);
        reminders.put(vinId, tempMap);
        portfolioTasks.setReminders(reminders);

        portfolioTasksRepo.save(portfolioTasks);
    }


    public void deleteGpDeadline(long vintageId, AllActivities allActivities){
        if(gpTasksRepo.findById(vintageId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(vintageId).get();
        HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
        if(deadlines==null){
            return;
        }


        deadlines.remove(allActivities);
        gpTasks.setDeadlines(deadlines);
        gpTasksRepo.save(gpTasks);
    }

    public void deleteLpDeadline(long lpRoundId, AllActivities allActivities){
        if(lpTasksRepo.findById(lpRoundId).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(lpRoundId).get();
        HashMap<AllActivities, Frequencies> deadlines=lpTasks.getDeadlines();
        if(deadlines==null){
            return;
        }
        deadlines.remove(allActivities);
        lpTasks.setDeadlines(deadlines);
        lpTasksRepo.save(lpTasks);
    }

    public void deletePortfolioDeadline(long pid, long vinId, AllActivities allActivities) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines = portfolioTasks.getDeadlines();
        if (deadlines == null) {
            return;
        }
        HashMap<AllActivities, Frequencies> tempMap = deadlines.get(vinId);

        tempMap.remove(allActivities);
        deadlines.put(vinId, tempMap);
        portfolioTasks.setDeadlines(deadlines);
        portfolioTasksRepo.save(portfolioTasks);
    }

    ///////////////////////////////////////////////////////

    public void deleteGpReminder(long userid, AllActivities allActivities){
        if(gpTasksRepo.findById(userid).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(userid).get();
        HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();
        if(reminders==null){
            return;
        }

        reminders.remove(allActivities);

        gpTasks.setReminders(reminders);
        gpTasksRepo.save(gpTasks);
    }

    public void deleteLpReminder(long lpRoundId, AllActivities allActivities){
        if(lpTasksRepo.findById(lpRoundId).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(lpRoundId).get();
        HashMap<AllActivities, Frequencies> reminders=lpTasks.getReminders();
        if(reminders==null){
            return;
        }
        reminders.remove(allActivities);
        lpTasks.setReminders(reminders);
        lpTasksRepo.save(lpTasks);
    }


    public void deletePortfolioReminder(long id, Long vinId, AllActivities allActivities) {
        if (portfolioTasksRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(id).get();

        HashMap<Long, HashMap<AllActivities, Frequencies>> reminders = portfolioTasks.getReminders();

        if (reminders == null) {
            return;
        }
        HashMap<AllActivities, Frequencies> tempMap = reminders.get(vinId);
        tempMap.remove(allActivities);
        portfolioTasks.setReminders(reminders);

        portfolioTasksRepo.save(portfolioTasks);
    }

    public List<AllActivities> getDeadlineReminderCritical(int gplpOrportfolio, Long id, Optional<Long> vinId) {

        LocalDateTime currTime = LocalDateTime.now();

        List<AllActivities> allActivities = new ArrayList<>();

        if (gplpOrportfolio == 1) {
            if (gpTasksRepo.findById(id).isEmpty()) {
                throw new RuntimeException("GP tasks not found");
            }
            GpTasks gpTasks = gpTasksRepo.findById(id).get();
            HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
            HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();

            if(deadlines!=null){
                for (AllActivities act :
                        deadlines.keySet()) {
                    Frequencies frequencies = deadlines.get(act);
                    LocalDateTime temp=frequencies.getLocalDateTime().minusDays(deadlineStart);
                    if(temp.isBefore(currTime)){
                        allActivities.add(act);
                    }
                }
            }

            if(reminders!=null){
                for (AllActivities act :
                        reminders.keySet()) {
                    Frequencies frequencies = reminders.get(act);
                    LocalDateTime temp=frequencies.getLocalDateTime().minusDays(reminderStart);
                    if(temp.isBefore(currTime)){
                        allActivities.add(act);
                    }
                }
            }
            return allActivities;
        }
        else if(gplpOrportfolio==2){
            if(lpTasksRepo.findById(id).isEmpty()){
                throw new RuntimeException("LP tasks not found");
            }
            LpTasks lpTasks=lpTasksRepo.findById(id).get();
            HashMap<AllActivities, Frequencies> deadlines=lpTasks.getDeadlines();
            HashMap<AllActivities, Frequencies> reminders=lpTasks.getReminders();


            if(deadlines!=null){
                for (AllActivities act :
                        deadlines.keySet()) {
                    Frequencies frequencies = deadlines.get(act);
                    LocalDateTime temp=frequencies.getLocalDateTime().minusDays(deadlineStart);
                    if(temp.isBefore(currTime)){
                        allActivities.add(act);
                    }
                }
            }

            if(reminders!=null){
                for (AllActivities act :
                        reminders.keySet()) {
                    Frequencies frequencies = reminders.get(act);
                    LocalDateTime temp=frequencies.getLocalDateTime().minusDays(reminderStart);
                    if(temp.isBefore(currTime)){
                        allActivities.add(act);
                    }
                }

            }

            return allActivities;

        }
        else if(gplpOrportfolio==3) {
            if (portfolioTasksRepo.findById(id).isEmpty()) {
                throw new RuntimeException("Portfolio tasks not found");
            }
            if (vinId.isEmpty())
                throw new RuntimeException("Vintage id must be specified");

            PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(id).get();
            HashMap<AllActivities, Frequencies> deadlines = portfolioTasks.getDeadlines().get(vinId.get());
            HashMap<AllActivities, Frequencies> reminders = portfolioTasks.getReminders().get(vinId.get());


            if (deadlines != null) {
                for (AllActivities act :
                        deadlines.keySet()) {
                    Frequencies frequencies = deadlines.get(act);
                    LocalDateTime temp = frequencies.getLocalDateTime().minusDays(deadlineStart);
                    if (temp.isBefore(currTime)) {
                        allActivities.add(act);
                    }
                }
            }

            if(reminders!=null){
                for (AllActivities act :
                        reminders.keySet()) {
                    Frequencies frequencies = reminders.get(act);
                    LocalDateTime temp=frequencies.getLocalDateTime().minusDays(reminderStart);
                    if(temp.isBefore(currTime)){
                        allActivities.add(act);
                    }
                }

            }

            return allActivities;
        }
        else{
            throw new RuntimeException("Invalid value for Gp Lp or Portfolio");
        }
    }


}


