package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.GpTasks;
import one.captl.RESTful.notification.model.enums.GpInfoTasks;
import one.captl.RESTful.notification.model.enums.VintageOneTimeDocs;
import one.captl.RESTful.notification.model.enums.VintageOngoing;
import one.captl.RESTful.notification.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/gp-tasks") // Tells the endpoint of the api
@RestController
@Tag(name="GP Tasks api",description = "Rest api Tasks end point for interacting with Notifications microservice")
public class GpTasksController {
    private final TasksService tasksService;
    @Autowired
    public GpTasksController(TasksService tasksService){
        this.tasksService = tasksService;
    }
    @Operation(
            summary = "Add GPtasks",
            description = "Adding gp tasks record "
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
    public String addGpTask(@RequestParam("id") Long id) {
        try {
            return tasksService.createGpTasks(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Gp tasks",
            description = "Get Gp tasks of given user id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = GpTasks.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}",produces = "application/json")
    public GpTasks getGPTask(@PathVariable Long id)  {
        try {
            return tasksService.getGptasks(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Gp tasks info",
            description = "Get Gp tasks info of given id"
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
    @GetMapping(value = "/{id}/info-tasks",produces = "application/json")
    public HashMap<GpInfoTasks, Boolean> getGPTaskInfos(@PathVariable Long id)  {
        try {
            return tasksService.getGptasksInfo(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update GP one time doc status",
            description = "Update GP one time doc status with given id"
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
    @PatchMapping(value = "/{id}/one-time-document-status")
    public void updateGPOneDocStatus(@PathVariable long id, @RequestParam("vintageOneTimeDocs") VintageOneTimeDocs vintageOneTimeDocs, @RequestParam("state") Boolean state) {
        try {
            tasksService.updateGPOneTimeDocStatus(id, vintageOneTimeDocs, state);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Update GP ongoing doc status",
            description = "Update GP ongoing doc status with given id"
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
    @PatchMapping(value = "/{id}/on-going-document-status")
    public void updateGPOngoingDocStatus(@PathVariable long id, @RequestParam("vintageOngoing") VintageOngoing vintageOngoing, @RequestParam("state") Boolean state) {
        try {
            tasksService.updateGPOngoingDocStatus(id, vintageOngoing, state);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }



    @Operation(
            summary = "Get Gp task ongoing docs status ",
            description = "Get Gp tasks ongoing docs status of given user id"
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
    @GetMapping(value = "/{id}/ongoingdocumentstatus",produces = "application/json")
    public HashMap<VintageOngoing, Boolean> getGPTaskOngoingStatus(@PathVariable Long id)  {
        try {
            return tasksService.getGptasksOngoing(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
    @Operation(
            summary = "Get Gp task one time docs status ",
            description = "Get Gp tasks one time docs status of given user id"
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
    @GetMapping(value = "/{id}/onetimedocumentstatus",produces = "application/json")
    public HashMap<VintageOneTimeDocs, Boolean> getGPTaskOneTimesStatus(@PathVariable Long id)  {
        try {
            return tasksService.getGptasksOneTime(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update GP info status",
            description = "Update GP info status with given id"
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
    @PatchMapping(value = "/{id}/infotasks")
    public void updateGPInfoStatus(@PathVariable long id, @RequestParam("infoTask")GpInfoTasks infoTask,@RequestParam("state") Boolean state,@RequestParam("triggeredBy")Long triggeredBy,@RequestParam("usersToNotify")  List<Long> usersToBeNotified) {
        try {
            tasksService.updateGPInfoStatus(id, infoTask, state,triggeredBy,usersToBeNotified);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Gp Task",
            description = "Delete GP task with given id"
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
    public Long deleteGPTask(@PathVariable long Id) {
        try {
            tasksService.deleteGpTask(Id);
            return Id;
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
}
