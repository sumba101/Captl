package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.Frequencies;
import one.captl.RESTful.notification.model.enums.*;
import one.captl.RESTful.notification.service.CriticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/critical") // Tells the endpoint of the api
@RestController
@Tag(name="Critical Activities api",description = "A Rest api for Critical activities end point for interacting with Notifications microservice")
public class CriticalController {
    private final CriticalService criticalService;

    @Autowired
    public CriticalController(CriticalService criticalService){
        this.criticalService = criticalService;
    }


    @Operation(
            summary = "Get Starred Tasks. Stipulate gpLpOrPortfolio (for gp fund =1, for lp round = 2, for portfolio round = 3)",
            description = "Get Starred tasks in vintage with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @GetMapping(value = "/{id}/critical",produces = "application/json")
    public List<AllActivities> getCriticalTasks(@PathVariable long id, @RequestParam(value = "vintageId", required = false) Optional<Long> vinId, @RequestParam("gpLpOrPortfolio") Integer gpLpPortfolio) {
        try {
            return criticalService.getDeadlineReminderCritical(gpLpPortfolio, id, vinId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Get Starred Tasks. Stipulate gpLpOrPortfolio (for gp fund =1, for lp round = 2, for portfolio round = 3)",
            description = "Get Starred tasks in vintage with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @GetMapping(value = "/{id}/star",produces = "application/json")
    public List<AllActivities> getStarredTasks(@PathVariable long id, @RequestParam("gpLpOrPortfolio") Integer gpLpPortfolio) {
        try {
            return criticalService.getStarredTasks(gpLpPortfolio, id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Star a Task. Stipulate gpLpOrPortfolio (for gp fund =1, for lp round = 2, for portfolio round = 3)",
            description = "Star a task in vintage with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{id}/star")
    public void updateStarTask(@PathVariable long id, @RequestParam(value = "portfolioInfoTask", required = false) Optional<PortfolioInfoTasks> portfolioInfoTask, @RequestParam(value = "portfolioOngoing", required = false) Optional<PortfolioOngoing> portfolioOngoing, @RequestParam(value = "portfolioOneTime", required = false) Optional<PortfolioOneTime> portfolioOneTime, @RequestParam("lpInfoTasks") Optional<LpInfoTasks> lpInfoTasks, @RequestParam("lpDoc") Optional<LpDocs> lpDoc, @RequestParam(value = "vintageOngoing", required = false) Optional<VintageOngoing> vintageOngoing, @RequestParam(value = "gpInfoTasks", required = false) Optional<GpInfoTasks> gpInfoTasks, @RequestParam(value = "vintageOneTimeDocs", required = false) Optional<VintageOneTimeDocs> vintageOneTimeDocs, @RequestParam("gpLpOrPortfolio") Integer gpLpPortfolio) {
        try {
            AllActivities temp;
            if (gpLpPortfolio == 1) {
                if (gpInfoTasks.isEmpty() && vintageOngoing.isEmpty() && vintageOneTimeDocs.isEmpty())
                    throw new RuntimeException("Need either infotask or one time doc or ongoing doc enum value");
                temp = gpInfoTasks.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> vintageOneTimeDocs.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(vintageOngoing.get().name())));

            } else if (gpLpPortfolio == 2) {
                if (lpInfoTasks.isEmpty() && lpDoc.isEmpty())
                    throw new RuntimeException("Need either infotask or one time doc enum value");
                temp = lpInfoTasks.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> AllActivities.valueOf(lpDoc.get().name()));

            } else if (gpLpPortfolio == 3) {
                if (portfolioInfoTask.isEmpty() && portfolioOneTime.isEmpty() && portfolioOngoing.isEmpty())
                    throw new RuntimeException("Need either portfolio infotask, portfolio one time doc or portfolio ongoing enum value");
                temp = portfolioInfoTask.map(portfolioInfoTasks -> AllActivities.valueOf(portfolioInfoTasks.name())).orElseGet(() -> portfolioOneTime.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(portfolioOngoing.get().name())));

            } else {
                throw new RuntimeException("Invalid value for Gp Lp or Portfolio");
            }
            criticalService.starTask(gpLpPortfolio, id, temp);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update GP task deadline. Provide one of the optional values ",
            description = "Update GP task deadline with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @PatchMapping(value = "/gp-tasks/{id}/deadline")
    public void updateGPTaskDeadline(@PathVariable long id, @RequestParam(value = "vintageOngoing", required = false) Optional<VintageOngoing> vintageOngoing, @RequestParam(value = "infoTask", required = false) Optional<GpInfoTasks> infoTask, @RequestParam(value = "vintageOneTimeDocs", required = false) Optional<VintageOneTimeDocs> vintageOneTimeDocs, @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(deadline);
            if (infoTask.isEmpty() && vintageOngoing.isEmpty() && vintageOneTimeDocs.isEmpty())
                throw new RuntimeException("Need either info task or one time doc or ongoing doc enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> vintageOneTimeDocs.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(vintageOngoing.get().name())));

            criticalService.updateGpDeadline(id, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Update LP task deadline. Provide one of the optional values ",
            description = "Update LP task deadline with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/lp-tasks/{id}/deadline")
    public void updateLPTaskDeadline(@PathVariable long id, @RequestParam("infoTask") Optional<LpInfoTasks> infoTask, @RequestParam("lpDoc") Optional<LpDocs> lpDoc, @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(deadline);
            if (infoTask.isEmpty() && lpDoc.isEmpty())
                throw new RuntimeException("Need either infotask or one time doc enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> AllActivities.valueOf(lpDoc.get().name()));
            criticalService.updateLpDeadline(id, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update Portfolio Task deadline. Provide one of the optional values ",
            description = "Update Portfolio Task with given id deadline"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/portfolio-tasks/{id}/deadline")
    public void updatePortfolioTasksDeadline(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam(value = "infoTask", required = false) Optional<PortfolioInfoTasks> infoTask, @RequestParam(value = "portfolioOngoing", required = false) Optional<PortfolioOngoing> portfolioOngoing, @RequestParam(value = "portfolioOneTime", required = false) Optional<PortfolioOneTime> portfolioOneTime, @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(deadline);

            if (infoTask.isEmpty() && portfolioOneTime.isEmpty() && portfolioOngoing.isEmpty())
                throw new RuntimeException("Need either portfolio infotask, portfolio one time doc or portfolio ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(portfolioInfoTasks -> AllActivities.valueOf(portfolioInfoTasks.name())).orElseGet(() -> portfolioOneTime.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(portfolioOngoing.get().name())));
            criticalService.updatePortfolioDeadline(id, vinId, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    // Reminder APIs


    @Operation(
            summary = "Update GP task Reminder. Provide one of the optional values",
            description = "Update GP task with given id Reminder"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/gp-tasks/{id}/reminder")
    public void updateGPTaskReminder(@PathVariable long id, @RequestParam(value = "vintageOngoing", required = false) Optional<VintageOngoing> vintageOngoing, @RequestParam(value = "vintageOneTimeDocs", required = false) Optional<VintageOneTimeDocs> vintageOneTimeDocs, @RequestParam("infoTask") Optional<GpInfoTasks> infoTask, @RequestParam("reminder") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminder, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(reminder);
            if (infoTask.isEmpty() && vintageOneTimeDocs.isEmpty() && vintageOngoing.isEmpty())
                throw new RuntimeException("Need either  infotask,  one time doc or  ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> vintageOneTimeDocs.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(vintageOngoing.get().name())));

            criticalService.updateGpReminder(id, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Update LP  Reminder. Provide one of the optional values ",
            description = "Update LP with given id Reminder"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/lp-tasks/{id}/reminder")
    public void updateLPTaskReminder(@PathVariable long id, @RequestParam(value = "infoTask", required = false) Optional<LpInfoTasks> infoTask, @RequestParam(value = "lpDoc", required = false) Optional<LpDocs> lpDoc, @RequestParam("reminder") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminder, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(reminder);
            if (infoTask.isEmpty() && lpDoc.isEmpty())
                throw new RuntimeException("Need either infotask, or doc enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> AllActivities.valueOf(lpDoc.get().name()));

            criticalService.updateLpReminder(id, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update Portfolio Reminder. Provide one of the optional values ",
            description = "Update Portfolio task with given id Reminder"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/portfolio-tasks/{id}/reminder")
    public void updatePortfolioTaskReminder(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam(value = "infoTask", required = false) Optional<PortfolioInfoTasks> infoTask, @RequestParam(value = "portfolioOngoing", required = false) Optional<PortfolioOngoing> portfolioOngoing, @RequestParam("portfolioOneTime") Optional<PortfolioOneTime> portfolioOneTime, @RequestParam("reminder") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminder, @RequestParam("freq") Freq freq) {
        try {
            Frequencies frequencies = new Frequencies();
            frequencies.setFreq(freq);
            frequencies.setLocalDateTime(reminder);
            if (infoTask.isEmpty() && portfolioOneTime.isEmpty() && portfolioOngoing.isEmpty())
                throw new RuntimeException("Need either portfolio infotask, portfolio one time doc or portfolio ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(portfolioInfoTasks -> AllActivities.valueOf(portfolioInfoTasks.name())).orElseGet(() -> portfolioOneTime.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(portfolioOngoing.get().name())));

            criticalService.updatePortfolioReminder(id, vinId, temp, frequencies);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

//    DELETION



    @Operation(
            summary = "Delete GP task deadline. Provide one of the optional values",
            description = "Delete GP task deadline with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @DeleteMapping(value = "/gp-tasks/{id}/deadline")
    public void deleteGPTaskDeadline(@PathVariable long id, @RequestParam(value = "vintageOngoing", required = false) Optional<VintageOngoing> vintageOngoing, @RequestParam(value = "vintageOneTimeDocs", required = false) Optional<VintageOneTimeDocs> vintageOneTimeDocs, @RequestParam("infoTask") Optional<GpInfoTasks> infoTask) {
        try {
            if (infoTask.isEmpty() && vintageOneTimeDocs.isEmpty() && vintageOngoing.isEmpty())
                throw new RuntimeException("Need either  infotask,  one time doc or  ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> vintageOneTimeDocs.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(vintageOngoing.get().name())));

            criticalService.deleteGpDeadline(id, temp);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete LP task deadline. Provide one of the optional values",
            description = "Delete LP task with given id deadline"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @DeleteMapping(value = "/lp-tasks/{id}/deadline")
    public void deleteLPTaskDeadline(@PathVariable long id, @RequestParam(value = "infoTask", required = false) Optional<LpInfoTasks> infoTask, @RequestParam(value = "lpDoc", required = false) Optional<LpDocs> lpDoc) {
        try {
            if (infoTask.isEmpty() && lpDoc.isEmpty())
                throw new RuntimeException("Need either infotask, or doc enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> AllActivities.valueOf(lpDoc.get().name()));
            criticalService.deleteLpDeadline(id, temp);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Portfolio task deadline. Provide one of the optional values",
            description = "Delete Portfolio task deadline with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @DeleteMapping(value = "/portfolio-tasks/{id}/deadline")
    public void deletePortfolioTaskDeadline(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam(value = "infoTask", required = false) Optional<PortfolioInfoTasks> infoTask, @RequestParam(value = "portfolioOngoing", required = false) Optional<PortfolioOngoing> portfolioOngoing, @RequestParam("portfolioOneTime") Optional<PortfolioOneTime> portfolioOneTime) {
        try {
            if (infoTask.isEmpty() && portfolioOneTime.isEmpty() && portfolioOngoing.isEmpty())
                throw new RuntimeException("Need either portfolio infotask, portfolio one time doc or portfolio ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(portfolioInfoTasks -> AllActivities.valueOf(portfolioInfoTasks.name())).orElseGet(() -> portfolioOneTime.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(portfolioOngoing.get().name())));
            criticalService.deletePortfolioDeadline(id, vinId, temp);

        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete GP Reminder. Provide one of the optional values",
            description = "Delete Reminder with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @DeleteMapping(value = "/gp-tasks/{id}/reminder")
    public void deleteGPTaskReminder(@PathVariable long id, @RequestParam(value = "vintageOngoing", required = false) Optional<VintageOngoing> vintageOngoing, @RequestParam(value = "vintageOneTimeDocs", required = false) Optional<VintageOneTimeDocs> vintageOneTimeDocs, @RequestParam("infoTask") Optional<GpInfoTasks> infoTask) {
        try {
            if (infoTask.isEmpty() && vintageOneTimeDocs.isEmpty() && vintageOngoing.isEmpty())
                throw new RuntimeException("Need either  infotask,  one time doc or  ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> vintageOneTimeDocs.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(vintageOngoing.get().name())));

            criticalService.deleteGpReminder(id, temp);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete LP Reminder. Provide one of the optional values",
            description = "Delete LP with given id Reminder"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @DeleteMapping(value = "/lp-tasks/{id}/reminder")
    public void deleteLPTaskReminder(@PathVariable long id, @RequestParam(value = "infoTask", required = false) Optional<LpInfoTasks> infoTask, @RequestParam(value = "lpDoc", required = false) Optional<LpDocs> lpDoc) {
        try {
            if (infoTask.isEmpty() && lpDoc.isEmpty())
                throw new RuntimeException("Need either infotask, or doc enum value");
            AllActivities temp;
            temp = infoTask.map(infoTasks -> AllActivities.valueOf(infoTasks.name())).orElseGet(() -> AllActivities.valueOf(lpDoc.get().name()));

            criticalService.deleteLpReminder(id, temp);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Portfolio Task Reminder. Provide one of the optional values",
            description = "Delete Portfolio task Reminder with given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @DeleteMapping(value = "/portfolio-tasks/{id}/reminder")
    public void deletePortfolioTaskReminder(@PathVariable long id, @RequestParam("vintageId") Long vinId, @RequestParam(value = "infoTask", required = false) Optional<PortfolioInfoTasks> infoTask, @RequestParam(value = "portfolioOngoing", required = false) Optional<PortfolioOngoing> portfolioOngoing, @RequestParam("portfolioOneTime") Optional<PortfolioOneTime> portfolioOneTime) {
        try {
            if (infoTask.isEmpty() && portfolioOneTime.isEmpty() && portfolioOngoing.isEmpty())
                throw new RuntimeException("Need either portfolio infotask, portfolio one time doc or portfolio ongoing enum value");
            AllActivities temp;
            temp = infoTask.map(portfolioInfoTasks -> AllActivities.valueOf(portfolioInfoTasks.name())).orElseGet(() -> portfolioOneTime.map(oneTime -> AllActivities.valueOf(oneTime.name())).orElseGet(() -> AllActivities.valueOf(portfolioOngoing.get().name())));

            criticalService.deletePortfolioReminder(id, vinId, temp);

        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

}
