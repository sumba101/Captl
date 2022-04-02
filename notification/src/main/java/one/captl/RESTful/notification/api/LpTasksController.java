package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.LpTasks;
import one.captl.RESTful.notification.model.enums.LpDocs;
import one.captl.RESTful.notification.model.enums.LpInfoTasks;
import one.captl.RESTful.notification.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/lp-tasks") // Tells the endpoint of the api
@RestController
@Tag(name = "Lp Tasks api", description = "Rest api Tasks end point for interacting with Notifications microservice")
public class LpTasksController {
    private final TasksService tasksService;
    @Autowired
    public LpTasksController(TasksService tasksService){
        this.tasksService = tasksService;
    }

    @Operation(
            summary = "Add LPtasks",
            description = "Adding lp tasks record "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @PostMapping(produces = "application/json")
    public String addLpTask(@RequestParam("id") Long id) {
        try {
            return tasksService.createLpTasks(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Lp tasks",
            description = "Get Lp tasks of given id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = LpTasks.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}",produces = "application/json")
    public LpTasks getLpTask(@PathVariable Long id)  {
        try {
            return tasksService.getLptasks(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Get Lp info tasks",
            description = "Get Lp info tasks of given id"
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
    public HashMap<LpInfoTasks, Boolean> getLpTaskInfos(@PathVariable Long id)  {
        try {
            return tasksService.getLptasksInfo(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Get Lp task one time docs status ",
            description = "Get Lp tasks one time docs status of given user id"
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
    public HashMap<LpDocs, Boolean> getLpTaskOneTimesStatus(@PathVariable Long id)  {
        try {
            return tasksService.getLptasksOneTime(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }



    @Operation(
            summary = "Update LP info status",
            description = "Update LP info status with given id"
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
    public void updateLPInfoStatus(@PathVariable long id, @RequestParam("infoTask") LpInfoTasks infoTask, @RequestParam("state") Boolean state, @RequestParam("triggeredBy") Long triggeredBy,@RequestParam("usersToBeNotified") List<Long> usersToBeNotified) {
        try {
            tasksService.updateLPInfoStatus(id, infoTask, state,triggeredBy,usersToBeNotified);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update LP doc status",
            description = "Update LP doc status with given id"
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
    @PatchMapping(value = "/{id}/onetimedocumentstatus")
    public void updateLPDocStatus(@PathVariable long id, @RequestParam("state") Boolean state, @RequestParam("lpDoc") LpDocs lpDoc) {
        try {
            tasksService.updateLPDocsStatus(id, state,lpDoc);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete Lp Task",
            description = "Delete LP task with given id"
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
    public Long deleteLPTask(@PathVariable long Id) {
        try {
            tasksService.deleteLpTask(Id);
            return Id;
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

}
