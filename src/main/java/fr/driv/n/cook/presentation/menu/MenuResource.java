package fr.driv.n.cook.presentation.menu;

import fr.driv.n.cook.presentation.menu.dto.Menu;
import fr.driv.n.cook.presentation.menu.dto.MenuItem;
import fr.driv.n.cook.presentation.menu.dto.MenuItemPatch;
import fr.driv.n.cook.presentation.menu.dto.MenuPatch;
import fr.driv.n.cook.service.menu.MenuService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MenuResource {

    @Inject
    MenuService menuService;

    @Inject
    JsonWebToken jsonWebToken;

    @POST
    public Menu createMenu() {
        return menuService.createMenu(currentFranchiseeId());
    }

    @GET
    @Path("/me")
    public Menu getMyMenu() {
        return menuService.listMenusForFranchisee(currentFranchiseeId()).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Aucun menu"));
    }

    @PATCH
    @Path("/me")
    public Menu updateMyMenu(@Valid MenuPatch menuPatch) {
        return menuService.patchMenu(currentMenuId(), menuPatch);
    }

    @GET
    @Path("/me/items")
    public List<MenuItem> listMenuItems() {
        return menuService.listItems(currentMenuId());
    }

    @POST
    @Path("/me/items")
    public MenuItem addMenuItem(@Valid MenuItem menuItem) {
        return menuService.createItem(currentMenuId(), menuItem);
    }

    @PATCH
    @Path("/me/items/{itemId}")
    public MenuItem updateMenuItem(
            @PathParam("itemId") Long itemId,
            @Valid MenuItemPatch menuItemPatch
    ) {
        return menuService.patchItem(itemId, menuItemPatch);
    }

    @DELETE
    @Path("/me/items/{itemId}")
    public void deleteMenuItem(@PathParam("itemId") Long itemId) {
        menuService.deleteItem(itemId);
    }

    private Long currentFranchiseeId() {
        String subject = jsonWebToken != null ? jsonWebToken.getSubject() : null;
        if (subject == null) {
            throw new NotAuthorizedException("Utilisateur non authentifié");
        }
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new NotAuthorizedException("Identifiant utilisateur invalide", ex);
        }
    }

    private Long currentMenuId() {
        return menuService.listMenusForFranchisee(currentFranchiseeId()).stream()
                .findFirst()
                .map(Menu::id)
                .orElseThrow(() -> new NotFoundException("Aucun menu"));
    }
}
