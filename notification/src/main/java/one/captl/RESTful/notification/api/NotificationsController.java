package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.Notification;
import one.captl.RESTful.notification.model.enums.*;
import one.captl.RESTful.notification.service.NotifServices;
import one.captl.RESTful.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/notifications") // Tells the endpoint of the api
@RestController
@Tag(name="Notfications api",description = "Rest api Notifications end point for interacting with Notifications microservice")
public class NotificationsController {
    private final NotificationService notificationService;
    private final NotifServices notifServices;
    @Autowired
    public NotificationsController(NotificationService notificationService,NotifServices notifServices){
        this.notificationService = notificationService;
        this.notifServices=notifServices;
    }

    @Operation(
            summary = "Add Docs Notification",
            description = "Adding Docs notification record"
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
    @PostMapping(value = "/documents",produces = "application/json")
    public String addDocsNotification(@RequestParam("userId") Long userid, @RequestParam("notificationType") NotificationType notificationType, @RequestParam("idType")IdType idType, @RequestParam("accessId") Long accessId,@RequestParam("documentType") DocumentType documentType,@RequestParam("docStatusType") DocStatusType docStatusType,@RequestParam("sharedNotification") Boolean sharedNotification,@RequestParam(value = "sharedToIdType",required = false) Optional<IdType> sharedToIdType,@RequestParam(value = "sharedToId",required = false) Optional<Long> sharedToId,@RequestParam("byEntityType")IdType byEntityType,@RequestParam("byEntityTypeAccessId")Long byEntityTypeAccessId) {
        try {
            if(!notificationType.equals(NotificationType.Doc)){
                throw new RuntimeException("notification type error");
            }
            Long id=notifServices.addEntryDoc(documentType, docStatusType, sharedNotification, sharedToIdType, sharedToId, byEntityType, byEntityTypeAccessId);
            return notificationService.addNotification(userid, notificationType,id,idType, accessId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Add Network Notification",
            description = "Adding Network notification record"
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
    @PostMapping(value = "/network",produces = "application/json")
    public String addNetworkNotification(@RequestParam("userId") Long userid, @RequestParam("notificationType") NotificationType notificationType, @RequestParam("byIdType")IdType idType, @RequestParam("byAccessId") Long accessId,@RequestParam("networkStatus") NetworkStatus networkStatus,@RequestParam("entityFrame") IdType entityFrame,@RequestParam("addedEntity") IdType addedEntity,@RequestParam("addedEntityAccessId") Long addedEntityAccessId) {
        try {
            if(!notificationType.equals(NotificationType.Network)){
                throw new RuntimeException("notification type error");
            }

            Long id=notifServices.addEntryNetwork(networkStatus, entityFrame, addedEntity, addedEntityAccessId);
            return notificationService.addNotification(userid, notificationType,id,idType, accessId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Add Filing Notification",
            description = "Adding Filing notification record"
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
    @PostMapping(value = "/filing",produces = "application/json")
    public String addFilingNotification(@RequestParam("userId") Long userid, @RequestParam("notificationType") NotificationType notificationType, @RequestParam("idType")IdType idType, @RequestParam("accessId") Long accessId,@RequestParam("byUserId")Long byUserId,@RequestParam("filingType")FilingType filingType) {
        try {
            if(!notificationType.equals(NotificationType.Filing)){
                throw new RuntimeException("notification type error");
            }

            Long id=notifServices.addEntryFiling(byUserId, filingType);
            return notificationService.addNotification(userid, notificationType,id,idType, accessId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Add Delegation Notification",
            description = "Adding Delegation notification record"
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
    @PostMapping(value = "/delegation",produces = "application/json")
    public String addDelegationNotification(@RequestParam("userId") Long userid, @RequestParam("notificationType") NotificationType notificationType, @RequestParam("idType")IdType idType, @RequestParam("accessId") Long accessId, @RequestParam("delegationId") Long delegationId, @RequestParam("delegatedStatus") DelegatedStatus delegatedStatus, @RequestParam("receiver") Boolean receiver, @RequestParam("masterEntityType") IdType masterEntityType, @RequestParam("masterEntityId") Long masterEntityId, @RequestParam("slaveEntityType") IdType slaveEntityType, @RequestParam("slaveEntityId") Long slaveEntityId, @RequestParam("delegationType") DelegationType delegationType, @RequestParam(value = "documentType",required = false) Optional<DocumentType>documentType, @RequestParam(value = "filingType",required = false) Optional<FilingType>filingType) {
        try {
            if(!notificationType.equals(NotificationType.Delegation)){
                throw new RuntimeException("notification type error");
            }

            Long id=notifServices.addEntryDelegation(delegationId,delegatedStatus, receiver, masterEntityType, masterEntityId, slaveEntityType, slaveEntityId, delegationType,false, documentType, filingType);
            return notificationService.addNotification(userid, notificationType,id,idType, accessId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }




    @Operation(
            summary = "Get all notifications",
            description = "Get all notifications of given user id"
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
    @GetMapping(value = "/{id}",produces = "application/json")
    public List<Notification> getAllNotificationFor(@PathVariable Long id)  {
        try {
            return notificationService.getAllNotificationsOf(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Get page of notifications",
            description = "Default pagenumber is 0, numberofresults is 10 and sortby is descending on notification time"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Notification.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/page/{id}",produces = "application/json")
    public Page<Notification> getPageofNotifications(@PathVariable Long id, @RequestParam(value = "pageNumber",required = false)Optional<Integer> pageno,@RequestParam(value = "numberOfResults",required = false)Optional<Integer> numberOfResults, @RequestParam(value = "sortBy",required = false)Optional<String> sortBy)  {
        try {
            return notificationService.getPageTopN(id,pageno,numberOfResults,sortBy);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }


    @Operation(
            summary = "Update Read status",
            description = "Update Read status with given id"
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
    @PatchMapping(value = "/{id}/read")
    public void updateReadStatus(@PathVariable long id) {
        try {
            notificationService.readNotification(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Update ALL Read status",
            description = "Update All Read status with given id"
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
    @PatchMapping(value = "/{id}/readall")
    public void updateReadAllStatus(@PathVariable long id) {
        try {
            notificationService.readAllNotifications(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete single notification",
            description = "Delete single notification with given id"
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
    @DeleteMapping(value = "/{Id}")
    public void deleteNotification(@PathVariable long Id) {
        try {
            notificationService.deleteNotification(Id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete All notifications ",
            description = "Delete all notifications from user with given id"
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
    @DeleteMapping(value = "/{userId}/all")
    public void deleteAllNotifications(@PathVariable Long userId) {
        try {
            notificationService.deleteAllNotificationsOf(userId);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

}