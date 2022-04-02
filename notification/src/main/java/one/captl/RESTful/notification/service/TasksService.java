package one.captl.RESTful.notification.service;

import one.captl.RESTful.notification.dao.GpTasksRepository;
import one.captl.RESTful.notification.dao.LpTasksRepository;
import one.captl.RESTful.notification.dao.PortfolioTasksRepository;
import one.captl.RESTful.notification.model.classes.Frequencies;
import one.captl.RESTful.notification.model.classes.GpTasks;
import one.captl.RESTful.notification.model.classes.LpTasks;
import one.captl.RESTful.notification.model.classes.PortfolioTasks;
import one.captl.RESTful.notification.model.enums.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TasksService {
    final
    NotificationService notificationService;
    final
    NotifServices notifServices;
    private final GpTasksRepository gpTasksRepo;
    private final LpTasksRepository lpTasksRepo;
    private final PortfolioTasksRepository portfolioTasksRepo;

    public TasksService(@Qualifier("gptasks") GpTasksRepository gpTasksRepo, @Qualifier("lptasks") LpTasksRepository lpTasksRepo, @Qualifier("portfoliotasks") PortfolioTasksRepository portfolioTasksRepo, NotificationService notificationService, NotifServices notifServices) {
        this.gpTasksRepo = gpTasksRepo;
        this.lpTasksRepo = lpTasksRepo;
        this.portfolioTasksRepo = portfolioTasksRepo;
        this.notificationService = notificationService;
        this.notifServices = notifServices;
    }
//  Addition functions

    public String createGpTasks(Long vinId) {
        GpTasks gpTasks = new GpTasks();
        gpTasks.setVintageRoundId(vinId);
        HashMap<VintageOneTimeDocs, Boolean> vintageOneTimeDocsBooleanHashMap = new HashMap<>();
        for (VintageOneTimeDocs f : VintageOneTimeDocs.values()) {
            vintageOneTimeDocsBooleanHashMap.put(f, false);
        }
        gpTasks.setGpOneTimeDocs(vintageOneTimeDocsBooleanHashMap);

        HashMap<VintageOngoing,Boolean> vintageOngoingBooleanHashMap=new HashMap<>();
        for(VintageOngoing f: VintageOngoing.values()){
            vintageOngoingBooleanHashMap.put(f,false);
        }
        gpTasks.setGpOngoingDocs(vintageOngoingBooleanHashMap);

        HashMap<GpInfoTasks,Boolean> gpInfoTasksBooleanHashMap=new HashMap<>();
        for (GpInfoTasks key: GpInfoTasks.values()){
            gpInfoTasksBooleanHashMap.put(key,false);
        }
        gpTasks.setInfoTasks(gpInfoTasksBooleanHashMap);


        gpTasks=gpTasksRepo.save(gpTasks);
        return String.valueOf(gpTasks.getVintageRoundId());
    }

    public String createLpTasks(long id){
        LpTasks lpTasks=new LpTasks();
        lpTasks.setLpRoundId(id);
        HashMap<LpInfoTasks,Boolean> lpInfoTasksBooleanHashMap=new HashMap<>();
        for (LpInfoTasks key: LpInfoTasks.values()){
            lpInfoTasksBooleanHashMap.put(key,false);
        }
        lpTasks.setInfoTasks(lpInfoTasksBooleanHashMap);

        HashMap<LpDocs,Boolean> lpDocsBooleanHashMap=new HashMap<>();
        for(LpDocs f: LpDocs.values()){
            lpDocsBooleanHashMap.put(f,false);
        }
        lpTasks.setLpOneTimeDocs(lpDocsBooleanHashMap);

        lpTasks=lpTasksRepo.save(lpTasks);

        return String.valueOf(lpTasks.getLpRoundId());
    }

    public String createPortfolioTasks(long id, Long vinId) {
        PortfolioTasks portfolioTasks;
        HashMap<Long, HashMap<PortfolioOneTime, Boolean>> portfolioOneTimeBooleanHashMap;
        HashMap<Long, HashMap<PortfolioOngoing, Boolean>> portfolioOngoingBooleanHashMap;

        if(portfolioTasksRepo.findById(id).isEmpty()){
            portfolioTasks = new PortfolioTasks();
            portfolioTasks.setPortfolioRoundId(id);
            HashMap<PortfolioInfoTasks, Boolean> infoTasksBooleanHashMap = new HashMap<>();
            for (PortfolioInfoTasks key : PortfolioInfoTasks.values()) {
                infoTasksBooleanHashMap.put(key, false);
            }
            portfolioTasks.setInfoTasks(infoTasksBooleanHashMap);
            HashMap<PortfolioRoundCreationDocs,Boolean> portfolioRoundCreationDocsBooleanHashMap = new HashMap<>();
            for (PortfolioRoundCreationDocs key : PortfolioRoundCreationDocs.values()) {
                portfolioRoundCreationDocsBooleanHashMap.put(key, false);
            }
            portfolioTasks.setPortfolioRoundCreationDocs(portfolioRoundCreationDocsBooleanHashMap);

            portfolioOneTimeBooleanHashMap = new HashMap<>();
            portfolioOngoingBooleanHashMap = new HashMap<>();
        }
        else{
            portfolioTasks = portfolioTasksRepo.findById(id).get();
            portfolioOneTimeBooleanHashMap = portfolioTasks.getPortfolioOneTimeDocs();
            portfolioOngoingBooleanHashMap = portfolioTasks.getPortfolioOngoingDocs();
        }

        HashMap<PortfolioOneTime, Boolean> tempMap = new HashMap<>();
        for (PortfolioOneTime key : PortfolioOneTime.values()) {
            tempMap.put(key, false);
        }
        portfolioOneTimeBooleanHashMap.put(vinId, tempMap);

        portfolioTasks.setPortfolioOneTimeDocs(portfolioOneTimeBooleanHashMap);


        HashMap<PortfolioOngoing, Boolean> tempMap2 = new HashMap<>();

        for (PortfolioOngoing key : PortfolioOngoing.values()) {
            tempMap2.put(key, false);
        }
        portfolioOngoingBooleanHashMap.put(vinId, tempMap2);
        portfolioTasks.setPortfolioOngoingDocs(portfolioOngoingBooleanHashMap);


        portfolioTasks = portfolioTasksRepo.save(portfolioTasks);
        return String.valueOf(portfolioTasks.getPortfolioRoundId());
    }

    //  Update functions

    ///////////////////////////////////////////////////// functions listed below
    public void updateGPInfoStatus(long vintageId, GpInfoTasks gpInfoTasks, Boolean state, Long triggeredBy, List<Long> usersToBeNotified){
        if(gpTasksRepo.findById(vintageId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(vintageId).get();
        HashMap<GpInfoTasks, Boolean> temp=gpTasks.getInfoTasks();
        temp.put(gpInfoTasks,state);
        gpTasks.setInfoTasks(temp);

        /// Next set for the deadline
        HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
        // Next set for the reminders
        HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies=deadlines.get(AllActivities.valueOf(gpInfoTasks.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.put(AllActivities.valueOf(gpInfoTasks.name()),frequencies);

                if(frequencies.getFreq()==Freq.None){
                    deadlines.remove(AllActivities.valueOf(gpInfoTasks.name()));
                }
                gpTasks.setDeadlines(deadlines);
            }
        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq=reminders.get(AllActivities.valueOf(gpInfoTasks.name()));
            if(reminFreq!=null){

                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.put(AllActivities.valueOf(gpInfoTasks.name()),reminFreq);

                if(reminFreq.getFreq()==Freq.None){
                    reminders.remove(AllActivities.valueOf(gpInfoTasks.name()));
                }
                gpTasks.setReminders(reminders);

            }
        }


        gpTasksRepo.save(gpTasks);

        if(state){

            Long id=notifServices.addEntryFiling(triggeredBy, FilingType.valueOf(gpInfoTasks.name()));

            for (Long uid :
                    usersToBeNotified) {
                    notificationService.addNotification(uid, NotificationType.Filing,id,IdType.VintageId, vintageId);
                }
            }
    }

    public void updateLPInfoStatus(long Id, LpInfoTasks lpInfoTasks, Boolean state, Long triggeredBy, List<Long> usersToBeNotified){
        if(lpTasksRepo.findById(Id).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(Id).get();
        HashMap<LpInfoTasks, Boolean> temp=lpTasks.getInfoTasks();
        temp.put(lpInfoTasks,state);
        lpTasks.setInfoTasks(temp);



        /// Next set for the deadline
        HashMap<AllActivities, Frequencies> deadlines=lpTasks.getDeadlines();
        // Next set for the reminders
        HashMap<AllActivities, Frequencies> reminders=lpTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies=deadlines.get(AllActivities.valueOf(lpInfoTasks.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.put(AllActivities.valueOf(lpInfoTasks.name()),frequencies);

                if(frequencies.getFreq()==Freq.None){
                    deadlines.remove(AllActivities.valueOf(lpInfoTasks.name()));
                }
                lpTasks.setDeadlines(deadlines);
            }
        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq=reminders.get(AllActivities.valueOf(lpInfoTasks.name()));
            if(reminFreq!=null){

                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.put(AllActivities.valueOf(lpInfoTasks.name()),reminFreq);

                if(reminFreq.getFreq()==Freq.None){
                    reminders.remove(AllActivities.valueOf(lpInfoTasks.name()));
                }
                lpTasks.setReminders(reminders);

            }
        }


        lpTasksRepo.save(lpTasks);

        if(state){
            Long id=notifServices.addEntryFiling(triggeredBy, FilingType.valueOf(lpInfoTasks.name()));

                for (Long uid :
                        usersToBeNotified) {
                        notificationService.addNotification(uid, NotificationType.Filing,id,IdType.LpRoundId, Id);
                }

        }
    }

    public void updatePortfolioInfoStatus(long pid, Long vinId, PortfolioInfoTasks portfolioInfoTasks, Boolean state, Long triggeredBy, List<Long> usersToBeNotified) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<PortfolioInfoTasks, Boolean> temp = portfolioTasks.getInfoTasks();
        temp.put(portfolioInfoTasks, state);
        portfolioTasks.setInfoTasks(temp);


        /// Next set for the deadline
        HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines = portfolioTasks.getDeadlines();
        // Next set for the reminders
        HashMap<Long, HashMap<AllActivities, Frequencies>> reminders = portfolioTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies = deadlines.get(vinId).get(AllActivities.valueOf(portfolioInfoTasks.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.get(vinId).put(AllActivities.valueOf(portfolioInfoTasks.name()), frequencies);

                if(frequencies.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = deadlines.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioInfoTasks.name()));
                    deadlines.put(vinId, tempMap);
                }
                portfolioTasks.setDeadlines(deadlines);
            }

        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq = reminders.get(vinId).get(AllActivities.valueOf(portfolioInfoTasks.name()));
            if(reminFreq!=null){
                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.get(vinId).put(AllActivities.valueOf(portfolioInfoTasks.name()), reminFreq);

                if(reminFreq.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = reminders.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioInfoTasks.name()));
                    reminders.put(vinId, tempMap);
                }
                portfolioTasks.setReminders(reminders);

            }
        }


        portfolioTasksRepo.save(portfolioTasks);

        if(state){
            Long id=notifServices.addEntryFiling(triggeredBy, FilingType.valueOf(portfolioInfoTasks.name()));
                for (Long uid :
                        usersToBeNotified) {
                    notificationService.addNotification(uid, NotificationType.Filing,id,IdType.PortfolioRoundId, pid);
                }

        }

    }

    public void updateGPOneTimeDocStatus(long vintageId, VintageOneTimeDocs vintageOneTimeDocs, Boolean state){
        if(gpTasksRepo.findById(vintageId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(vintageId).get();
        HashMap<VintageOneTimeDocs, Boolean> temp=gpTasks.getGpOneTimeDocs();
        temp.put(vintageOneTimeDocs,state);
        gpTasks.setGpOneTimeDocs(temp);


        /// Next set for the deadline
        HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
        // Next set for the reminders
        HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies=deadlines.get(AllActivities.valueOf(vintageOneTimeDocs.name()));
            if(frequencies!=null){
                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.put(AllActivities.valueOf(vintageOneTimeDocs.name()),frequencies);

                if(frequencies.getFreq()==Freq.None){
                    deadlines.remove(AllActivities.valueOf(vintageOneTimeDocs.name()));
                }
                gpTasks.setDeadlines(deadlines);
            }
        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq=reminders.get(AllActivities.valueOf(vintageOneTimeDocs.name()));
            if(reminFreq!=null){

                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.put(AllActivities.valueOf(vintageOneTimeDocs.name()),reminFreq);

                if(reminFreq.getFreq()==Freq.None){
                    reminders.remove(AllActivities.valueOf(vintageOneTimeDocs.name()));
                }
                gpTasks.setReminders(reminders);
            }

        }


        gpTasksRepo.save(gpTasks);

    }

    public void updateLPDocsStatus(long Id, Boolean state, LpDocs lpDocs){
        if(lpTasksRepo.findById(Id).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        LpTasks lpTasks=lpTasksRepo.findById(Id).get();
        HashMap<LpDocs, Boolean> temp=lpTasks.getLpOneTimeDocs();
        temp.put(lpDocs,state);
        lpTasks.setLpOneTimeDocs(temp);

        /// Next set for the deadline
        HashMap<AllActivities, Frequencies> deadlines=lpTasks.getDeadlines();
        // Next set for the reminders
        HashMap<AllActivities, Frequencies> reminders=lpTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies=deadlines.get(AllActivities.valueOf(lpDocs.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.put(AllActivities.valueOf(lpDocs.name()),frequencies);

                if(frequencies.getFreq()==Freq.None){
                    deadlines.remove(AllActivities.valueOf(lpDocs.name()));
                }
                lpTasks.setDeadlines(deadlines);
            }

        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq=reminders.get(AllActivities.valueOf(lpDocs.name()));
            if(reminFreq!=null){

                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.put(AllActivities.valueOf(lpDocs.name()),reminFreq);

                if(reminFreq.getFreq()==Freq.None){
                    reminders.remove(AllActivities.valueOf(lpDocs.name()));
                }
                lpTasks.setReminders(reminders);
            }

        }


        lpTasksRepo.save(lpTasks);
    }

    public void updatePortfolioOneTimeDocsStatus(long pid, Long vinId, PortfolioOneTime portfolioOneTime, Boolean state) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<Long, HashMap<PortfolioOneTime, Boolean>> temp = portfolioTasks.getPortfolioOneTimeDocs();
        temp.get(vinId).put(portfolioOneTime, state);
        portfolioTasks.setPortfolioOneTimeDocs(temp);

        /// Next set for the deadline
        HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines = portfolioTasks.getDeadlines();
        // Next set for the reminders
        HashMap<Long, HashMap<AllActivities, Frequencies>> reminders = portfolioTasks.getReminders();

        if (deadlines != null) { // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies = deadlines.get(vinId).get(AllActivities.valueOf(portfolioOneTime.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.get(vinId).put(AllActivities.valueOf(portfolioOneTime.name()), frequencies);

                if(frequencies.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = deadlines.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioOneTime.name()));
                    deadlines.put(vinId, tempMap);
                }
                portfolioTasks.setDeadlines(deadlines);
            }

        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq = reminders.get(vinId).get(AllActivities.valueOf(portfolioOneTime.name()));
            if(reminFreq!=null){


                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.get(vinId).put(AllActivities.valueOf(portfolioOneTime.name()), reminFreq);

                if(reminFreq.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = reminders.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioOneTime.name()));
                    reminders.put(vinId, tempMap);
                }
                portfolioTasks.setReminders(reminders);
            }
        }

        portfolioTasksRepo.save(portfolioTasks);
    }

    public void updatePortfolioCreationDocsStatus(long pid, PortfolioRoundCreationDocs portfolioRoundCreationDocs, Boolean state) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<PortfolioRoundCreationDocs, Boolean> temp = portfolioTasks.getPortfolioRoundCreationDocs();
        temp.put(portfolioRoundCreationDocs, state);
        portfolioTasks.setPortfolioRoundCreationDocs(temp);
        portfolioTasksRepo.save(portfolioTasks);
    }

    public void updateGPOngoingDocStatus(long vintageId, VintageOngoing vintageOngoing, Boolean state){
        if(gpTasksRepo.findById(vintageId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        GpTasks gpTasks=gpTasksRepo.findById(vintageId).get();
        HashMap<VintageOngoing, Boolean> temp=gpTasks.getGpOngoingDocs();
        temp.put(vintageOngoing,state);

        gpTasks.setGpOngoingDocs(temp);

        /// Next set for the deadline
        HashMap<AllActivities, Frequencies> deadlines=gpTasks.getDeadlines();
        // Next set for the reminders
        HashMap<AllActivities, Frequencies> reminders=gpTasks.getReminders();

        if(deadlines!=null){ // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies=deadlines.get(AllActivities.valueOf(vintageOngoing.name()));

            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.put(AllActivities.valueOf(vintageOngoing.name()),frequencies);

                if(frequencies.getFreq()==Freq.None){
                    deadlines.remove(AllActivities.valueOf(vintageOngoing.name()));
                }
                gpTasks.setDeadlines(deadlines);
            }

        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq=reminders.get(AllActivities.valueOf(vintageOngoing.name()));
            if(reminFreq!=null){


                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.put(AllActivities.valueOf(vintageOngoing.name()),reminFreq);

                if(reminFreq.getFreq()==Freq.None){
                    reminders.remove(AllActivities.valueOf(vintageOngoing.name()));
                }
                gpTasks.setReminders(reminders);
            }
        }

        gpTasksRepo.save(gpTasks);

    }

    public void updatePortfolioOngoingDocsStatus(long pid, Long vinId, PortfolioOngoing portfolioOngoing, Boolean state) {
        if (portfolioTasksRepo.findById(pid).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        PortfolioTasks portfolioTasks = portfolioTasksRepo.findById(pid).get();
        HashMap<Long, HashMap<PortfolioOngoing, Boolean>> temp = portfolioTasks.getPortfolioOngoingDocs();
        temp.get(vinId).put(portfolioOngoing, state);
        portfolioTasks.setPortfolioOngoingDocs(temp);


        /// Next set for the deadline
        HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines = portfolioTasks.getDeadlines();
        // Next set for the reminders
        HashMap<Long, HashMap<AllActivities, Frequencies>> reminders = portfolioTasks.getReminders();

        if (deadlines != null) { // If this task has a deadline on it, morph it accordingly
            Frequencies frequencies = deadlines.get(vinId).get(AllActivities.valueOf(portfolioOngoing.name()));
            if(frequencies!=null){

                if(state){
                    frequencies.nextSet();
                }else{
                    frequencies.backSet();
                }
                deadlines.get(vinId).put(AllActivities.valueOf(portfolioOngoing.name()), frequencies);

                if(frequencies.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = deadlines.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioOngoing.name()));
                    deadlines.put(vinId, tempMap);
                }
                portfolioTasks.setDeadlines(deadlines);
            }
        }

        if(reminders!=null){ // If this task has a reminder on it, morph it accordingly
            Frequencies reminFreq = reminders.get(vinId).get(AllActivities.valueOf(portfolioOngoing.name()));
            if(reminFreq!=null){

                if(state){
                    reminFreq.nextSet();
                }else{
                    reminFreq.backSet();
                }
                reminders.get(vinId).put(AllActivities.valueOf(portfolioOngoing.name()), reminFreq);

                if(reminFreq.getFreq()==Freq.None) {
                    HashMap<AllActivities, Frequencies> tempMap = reminders.get(vinId);
                    tempMap.remove(AllActivities.valueOf(portfolioOngoing.name()));
                    reminders.put(vinId, tempMap);

                }
                portfolioTasks.setReminders(reminders);
            }

        }

        portfolioTasksRepo.save(portfolioTasks);
    }

    //  Getter functions
    public GpTasks getGptasks(long userId){
        if(gpTasksRepo.findById(userId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        return gpTasksRepo.findById(userId).get();
    }
    public LpTasks getLptasks(long Id){
        if(lpTasksRepo.findById(Id).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        return lpTasksRepo.findById(Id).get();
    }
    public PortfolioTasks getPortfoliotasks(long id){
        if(portfolioTasksRepo.findById(id).isEmpty()){
            throw new RuntimeException("Portfolio tasks not found");
        }
        return portfolioTasksRepo.findById(id).get();
    }

    public HashMap<GpInfoTasks, Boolean> getGptasksInfo(long userId){
        if(gpTasksRepo.findById(userId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        return gpTasksRepo.findById(userId).get().getInfoTasks();
    }
    public HashMap<LpInfoTasks, Boolean> getLptasksInfo(long Id){
        if(lpTasksRepo.findById(Id).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        return lpTasksRepo.findById(Id).get().getInfoTasks();
    }
    public HashMap<PortfolioInfoTasks, Boolean> getPortfoliotasksInfo(long id){
        if(portfolioTasksRepo.findById(id).isEmpty()){
            throw new RuntimeException("Portfolio tasks not found");
        }
        return portfolioTasksRepo.findById(id).get().getInfoTasks();
    }

    public HashMap<VintageOneTimeDocs, Boolean> getGptasksOneTime(long userId){
        if(gpTasksRepo.findById(userId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        return gpTasksRepo.findById(userId).get().getGpOneTimeDocs();
    }

    public HashMap<LpDocs, Boolean> getLptasksOneTime(long Id) {
        if (lpTasksRepo.findById(Id).isEmpty()) {
            throw new RuntimeException("LP tasks not found");
        }
        return lpTasksRepo.findById(Id).get().getLpOneTimeDocs();
    }

    public HashMap<Long, HashMap<PortfolioOneTime, Boolean>> getPortfoliotasksOneTime(long id) {
        if (portfolioTasksRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        return portfolioTasksRepo.findById(id).get().getPortfolioOneTimeDocs();
    }

    public HashMap<VintageOngoing, Boolean> getGptasksOngoing(long userId){
        if(gpTasksRepo.findById(userId).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        return gpTasksRepo.findById(userId).get().getGpOngoingDocs();
    }

    public HashMap<Long, HashMap<PortfolioOngoing, Boolean>> getPortfoliotasksOngoing(long id) {
        if (portfolioTasksRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Portfolio tasks not found");
        }
        return portfolioTasksRepo.findById(id).get().getPortfolioOngoingDocs();
    }

    //  Delete functions
    public void deleteGpTask(long id){
        if(gpTasksRepo.findById(id).isEmpty()){
            throw new RuntimeException("GP tasks not found");
        }
        gpTasksRepo.deleteById(id);
    }

    public void deleteLpTask(long id){
        if(lpTasksRepo.findById(id).isEmpty()){
            throw new RuntimeException("LP tasks not found");
        }
        lpTasksRepo.deleteById(id);
    }

    public void deletePortfolioTask(long id){
        if(portfolioTasksRepo.findById(id).isEmpty()){
            throw new RuntimeException("Portfolio tasks not found");
        }
        portfolioTasksRepo.deleteById(id);
    }

}
