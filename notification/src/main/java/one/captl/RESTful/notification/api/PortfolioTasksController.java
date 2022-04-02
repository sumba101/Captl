package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.PortfolioTasks;
import one.captl.RESTful.notification.model.enums.PortfolioInfoTasks;
import one.captl.RESTful.notification.model.enums.PortfolioOneTime;
import one.captl.RESTful.notification.model.enums.PortfolioOngoing;
import one.captl.RESTful.notification.model.enums.PortfolioRoundCreationDocs;
import one.captl.RESTful.notification.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RequestMapping("/portfolio-tasks") // Tells the endpoint of the api
@RestController
@Tag(name = "Portfolio Tasks api", description = "Rest api Tasks end point for interacting with Notifications microservice")
public class PortfolioTasksController {
    private final TasksService tasksService;
    @Autowired
    public PortfolioTasksController(TasksService tasksService){
        this.tasksService = tasksService;
    }

    @Operation(
            summary = "Add Portfolio tasks",
            description = "Adding portfolio tasks record "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @PostMapping(produces = "application/json")
    public String addPortfolioTasks(@RequestParam("id") Long id, @RequestParam("vintages") Long vintages) {
        try {
            return tasksService.createPortfolioTasks(id, vintages);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }



    @Operation(
            summary = "Get Portfolio tasks",
            description = "Get Portfolio tasks of given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = PortfolioTasks.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}",produces = "application/json")
    public PortfolioTasks getPortfolioTask(@PathVariable Long id)  {
        try {
            return tasksService.getPortfoliotasks(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }



    @Operation(
            summary = "Update Portfolio info status",
            description = "Update Portfolio info status with given id"
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
    @PatchMapping(value = "/{id}/infotasks")
    public void updatePortfolioInfoStatus(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam("infoTask") PortfolioInfoTasks infoTask, @RequestParam("state") Boolean state, @RequestParam("triggeredBy") Long triggeredBy, @RequestParam("usersToBeNotified") List<Long> usersToBeNotified) {
        try {
            tasksService.updatePortfolioInfoStatus(id, vinId, infoTask, state, triggeredBy, usersToBeNotified);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update Portfolio one time doc status",
            description = "Update Portfolio one time doc status with given id"
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
    @PatchMapping(value = "/{id}/onetimedocumentstatus")
    public void updatePortfolioOnetimeDocStatus(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam("portfolioOneTime") PortfolioOneTime portfolioOneTime, @RequestParam("state") Boolean state) {
        try {
            tasksService.updatePortfolioOneTimeDocsStatus(id, vinId, portfolioOneTime, state);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update Portfolio creation doc status",
            description = "Update Portfolio creation doc status with given id"
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
    @PatchMapping(value = "/{id}/creation-document")
    public void updatePortfolioCreationDocStatus(@PathVariable long id, @RequestParam("portfolioRoundCreationDocs") PortfolioRoundCreationDocs portfolioRoundCreationDocs, @RequestParam("state") Boolean state) {
        try {
            tasksService.updatePortfolioCreationDocsStatus(id, portfolioRoundCreationDocs, state);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Update Portfolio ongoing doc status",
            description = "Update Portfolio ongoing doc status with given id"
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
    @PatchMapping(value = "/{id}/ongoingdocumentstatus")
    public void updatePortfolioOngoingDocStatus(@PathVariable long id, @RequestParam("vinId") Long vinId, @RequestParam("portfolioOngoing") PortfolioOngoing portfolioOngoing, @RequestParam("state") Boolean state) {
        try {
            tasksService.updatePortfolioOngoingDocsStatus(id, vinId, portfolioOngoing, state);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }



    @Operation(
            summary = "Get Portfolio task one time docs status ",
            description = "Get Portfolio tasks one time docs status of given user id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}/onetimedocumentstatus", produces = "application/json")
    public HashMap<Long, HashMap<PortfolioOneTime, Boolean>> getPortfolioTaskOneTimesStatus(@PathVariable Long id) {
        try {
            return tasksService.getPortfoliotasksOneTime(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Portfolio task ongoing docs status ",
            description = "Get Portfolio tasks ongoing docs status of given user id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}/ongoingdocumentstatus", produces = "application/json")
    public HashMap<Long, HashMap<PortfolioOngoing, Boolean>> getPortfolioTaskOngoingStatus(@PathVariable Long id) {
        try {
            return tasksService.getPortfoliotasksOngoing(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Portfolio Info tasks",
            description = "Get Portfolio Info tasks of given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = HashMap.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}/infotasks",produces = "application/json")
    public HashMap<PortfolioInfoTasks, Boolean> getPortfolioTaskInfos(@PathVariable Long id)  {
        try {
            return tasksService.getPortfoliotasksInfo(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete Portfolio Task",
            description = "Delete Portfolio task with given id"
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
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @DeleteMapping(value = "/{Id}",produces = "application/json")
    public Long deletePortfolioTask(@PathVariable long Id) {
        try {
            tasksService.deletePortfolioTask(Id);
            return Id;
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
}
