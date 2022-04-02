package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.*;
import one.captl.RESTful.notification.model.enums.*;
import one.captl.RESTful.notification.service.TaskDelegationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/delegations") // Tells the endpoint of the api
@RestController
@Tag(name="Task Delegation api",description = "A Rest api task delegation end point for interacting with Notifications microservice")
public class DelegationController {
    private final TaskDelegationService taskDelegationService;
    @Autowired
    public DelegationController(TaskDelegationService taskDelegationService){
        this.taskDelegationService = taskDelegationService;
    }

    @Operation(
            summary = "Creating delegated task",
            description = "Create a task delegation"
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
    public String addTaskDel(@RequestParam("delegationType") DelegationType delegationType, @RequestParam("idType") IdType idType, @RequestParam("accessId") Long accessId, @RequestParam("assignedBy") Long assignedBy, @RequestParam("assignedToIdType") IdType assignedToIdType, @RequestParam("assignedToId") Long assignedTo, @RequestParam(value = "filingType", required = false) Optional<FilingType> filingType, @RequestParam("slaveUsersToBeNotified") List<Long> slaveUsersToBeNotified, @RequestParam("masterUsersToBeNotified") List<Long> masterUsersToBeNotified, @RequestParam(value = "documentType",required = false) Optional<DocumentType> documentType, @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME ) LocalDateTime deadline) {
        try {
            return taskDelegationService.addDelegatedTask(delegationType,idType,accessId,assignedBy,assignedToIdType ,assignedTo, filingType,documentType,masterUsersToBeNotified,slaveUsersToBeNotified,deadline);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve by assigned to",
            description = "Retrieve all delegated tasks using assigned to"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/assignee/{assignee}", produces = "application/json")
    public List<Delegations> getAssignedTo(@PathVariable Long assignee, @RequestParam("assignedToIdType") IdType assignedToIdType) {
        try {
            return taskDelegationService.getByAssignedTo(assignee, assignedToIdType);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve by assigned by",
            description = "Retrieve all delegated tasks using assigned by"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/assignor/{assignor}",produces = "application/json")
    public List<Delegations> getAssignedBy(@PathVariable Long assignor)  {
        try {
            return taskDelegationService.getByAssignedBy(assignor);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Retrieve Delegated Task",
            description = "Retrieve delegated task by task id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Delegations.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}",produces = "application/json")
    public Delegations getTaskWithId(@PathVariable long id)  {
        try {
            return taskDelegationService.getById(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve the completed object of Delegated Task",
            description = "Retrieve the completed object of delegated task by task id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Object.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/{id}/delegation-object",produces = "application/json")
    public Object getDelegationObjectWithId(@PathVariable long id,@RequestParam("delegationType") DelegationType delegationType)  {
        try {
            return taskDelegationService.getDelegationObject(id,delegationType);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Change assigned to",
            description = "Change assigned to for a task"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/assignedto")
    public void changedAssignedTo(@PathVariable long ID,@RequestParam("assignedToIdType") IdType assignedToIdType,@RequestParam("assignedTo") Long assignedTo) {
        try {
            taskDelegationService.updateAssignedTo(ID,assignedToIdType,assignedTo);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Change delegation type",
            description = "Change delegation type for a task. Can be used to change the sub-task in InfoFiling or the document type in Document delegations. Can also be used to change from an infofiling delegation to document delegation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/delegation-type")
    public void changedDelegationType(@PathVariable long ID, @RequestParam("newTask") AllActivities newTask, @RequestParam("delegationType")DelegationType delegationType) {
        try {
            taskDelegationService.updateDelegationTask(ID, newTask, delegationType);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on info-filling completion",
            description = "Update task object on info-filling task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/info-completion")
    public void changedInfoCompletion(@PathVariable long ID, @RequestBody InfoFilling infoFilling) {
        try {
            taskDelegationService.updateInfoTaskCompletion(ID,infoFilling);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on captable completion",
            description = "Update task object on captable task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/captable-completion")
    public void changedCapTableCompletion(@PathVariable long ID, @RequestBody Captable captable) {
        try {
            taskDelegationService.updateCaptableCompletion(ID,captable);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on establishment Expenses completion",
            description = "Update task object on establishment Expenses task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/establishment-expenses-completion")
    public void changedEstablishmentFeesCompletion(@PathVariable long ID,
                                                   @RequestBody EstablishmentExpenses establishmentExpenses
    ) {
        try {
            taskDelegationService.updateEstablishmentFeesCompletion(ID, establishmentExpenses);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on operational Expenses completion",
            description = "Update task object on operational Expenses task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/operational-expenses-completion")
    public void changedOperationalFeesCompletion(@PathVariable long ID, @RequestBody OperationalExpenses operationalExpenses) {
        try {
            taskDelegationService.updateOperationalFeesCompletion(ID, operationalExpenses);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on portfolio-investment completion",
            description = "Update task object on portfolio-investment task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/portfolio-investment-completion")
    public void changedPortfolioInvestmentsCompletion(@PathVariable long ID, @RequestBody PortfolioInvestments portfolioInvestments) {
        try {
            taskDelegationService.updatePortfolioInvestmentFeesCompletion(ID, portfolioInvestments);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on distribution-waterfall completion",
            description = "Update task object on distribution-waterfall task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/distribution-waterfall-completion")
    public void distributionWaterfallCompletion(@PathVariable long ID,
                                                @RequestBody WaterFall waterFall) {

        try {
            taskDelegationService.updateDistributionWaterfallCompletion(ID, waterFall);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on management-fees completion",
            description = "Update task object on management-fees task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/management-fees-completion")
    public void changedManagementFeesCompletion(@PathVariable long ID,
                                                @RequestBody ManagementFees managementFees) {
        try {
            taskDelegationService.updateManagementFeesCompletion(ID, managementFees);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update deadline of task ",
            description = "Update task deadline object "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/deadline")
    public void changeDeadline(@PathVariable long ID, @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )  LocalDateTime deadline) {
        try {
            taskDelegationService.updateDeadline(ID,deadline);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Add slave subscribers of task ",
            description = "Add slave subscribers of Task "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/slave-subscribers")
    public void addMasterSubscriberList(@PathVariable long ID, @RequestParam("additionSlaves") List<Long> addedSlaves) {
        try {
            taskDelegationService.addSlaveSubscriber(ID,addedSlaves);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Add master subscribers of task ",
            description = "Add master subscribers of Task "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/master-subscribers")
    public void addSlaveSubscriberList(@PathVariable long ID, @RequestParam("additionMasters") List<Long> addedMasters) {
        try {
            taskDelegationService.addMasterSubscriber(ID,addedMasters);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update task on document completion",
            description = "Update task object on document task completion"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))

                    )
            }
    )
    @PatchMapping(value = "/{ID}/document-completion")
    public void changedDocCompletion(@PathVariable long ID, @RequestParam("docId") Long docId) {
        try {
            taskDelegationService.updateDocTaskCompletion(ID, docId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch (RuntimeException e) {
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Delete all tasks by assigned by",
            description = "Delete all delegated tasks with assigned by of given ID"
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
    @DeleteMapping(value = "/assignedby/{id}")
    public void deleteAllAssignedBy(@PathVariable Long id) {
        try {
            taskDelegationService.deleteByAssignedBy(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete all tasks by assigned to",
            description = "Delete all delegated tasks with assigned to of given ID"
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
    @DeleteMapping(value = "/assignedto/{id}")
    public void deleteAllAssignedTo(@PathVariable Long id,@RequestParam("assignedToIdType") IdType assignedToIdType) {
        try {
            taskDelegationService.deleteByAssignedTo(assignedToIdType,id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Delegated Task",
            description = "Delete Delegated Task record with given id"
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
    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable long id) {
        try {
            taskDelegationService.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Master subscriber",
            description = "Delete Master subscriber from Delegated Task record with given id"
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
    @DeleteMapping(value = "/{id}/master-subscribers")
    public void deleteMasterId(@PathVariable long id,@RequestParam("masterSubscriber")Long uid) {
        try {
            taskDelegationService.deleteMasterSubscriber(id,uid);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Slave subscriber",
            description = "Delete Slave subscriber from Delegated Task record with given id"
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
    @DeleteMapping(value = "/{id}/slave-subscribers")
    public void deleteSlaveId(@PathVariable long id,@RequestParam("slaveSubscriber")Long uid) {
        try {
            taskDelegationService.deleteSlaveSubscriber(id,uid);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
}