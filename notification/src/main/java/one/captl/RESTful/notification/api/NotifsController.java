package one.captl.RESTful.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.captl.RESTful.notification.exception.ApiException;
import one.captl.RESTful.notification.exception.DefaultExceptionHandler;
import one.captl.RESTful.notification.model.classes.Notifs.DelegationNotifications;
import one.captl.RESTful.notification.model.classes.Notifs.Docs;
import one.captl.RESTful.notification.model.classes.Notifs.Filings;
import one.captl.RESTful.notification.model.classes.Notifs.Network;
import one.captl.RESTful.notification.model.enums.NotificationType;
import one.captl.RESTful.notification.service.NotifServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notifs") // Tells the endpoint of the api
@RestController
@Tag(name="Notifs api",description = "A Rest api task delegation end point for interacting with Notifs in Notifications microservice")
public class NotifsController {
    private final NotifServices notifServices;
    @Autowired
    public NotifsController(NotifServices notifServices){
        this.notifServices = notifServices;
    }

    @Operation(
            summary = "Retrieve Docs notif by id",
            description = "Retrieve Docs notif by id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Docs.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/documents/{id}",produces = "application/json")
    public Docs getDocsNotif(@PathVariable Long id)  {
        try {
            return notifServices.getDocsEntity(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve Filings notif by id",
            description = "Retrieve Filings notif by id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Filings.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/filings/{id}",produces = "application/json")
    public Filings getFilingsNotif(@PathVariable Long id)  {
        try {
            return notifServices.getFilingsEntity(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve Delegation notif by id",
            description = "Retrieve Delegation notif by id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = DelegationNotifications.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/delegation/{id}",produces = "application/json")
    public DelegationNotifications getDelegationsNotif(@PathVariable Long id)  {
        try {
            return notifServices.getDelegationNotifsEntity(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve Network notif by id",
            description = "Retrieve Network notif by id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Network.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bad request",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ApiException.class))
                    )
            }
    )
    @GetMapping(value = "/network/{id}",produces = "application/json")
    public Network getNetworkNotif(@PathVariable Long id)  {
        try {
            return notifServices.getNetworkEntity(id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
    @Operation(
            summary = "Delete single notif",
            description = "Delete single notif with given id"
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
    public void deleteNotification(@PathVariable long Id, @RequestParam("notificationType")NotificationType notificationType) {
        try {
            notifServices.deleteSingleNotif(notificationType,Id);
        } catch (IllegalArgumentException e) {
            throw new DefaultExceptionHandler("Incorrect parameters");
        } catch(RuntimeException e){
            throw new DefaultExceptionHandler(e.getMessage());
        }
    }
}
