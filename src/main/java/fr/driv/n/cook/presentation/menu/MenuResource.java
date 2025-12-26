package fr.driv.n.cook.presentation.menu;

import fr.driv.n.cook.presentation.menu.dto.Menu;
import fr.driv.n.cook.presentation.menu.dto.MenuItem;
import fr.driv.n.cook.presentation.menu.dto.MenuItemPatch;
import fr.driv.n.cook.presentation.menu.dto.MenuPatch;
import fr.driv.n.cook.presentation.shared.dto.MenuStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Path("/menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MenuResource {

    @POST
    public Menu createMenu(@Valid Menu menu) {
        return new Menu(1L, MenuStatus.DRAFT, LocalDateTime.now());
    }

    @GET
    @Path("/me")
    public Menu getMyMenu() {
        return new Menu(1L, MenuStatus.PUBLISHED, LocalDateTime.now());
    }

    @PATCH
    @Path("/me")
    public Menu updateMyMenu(@Valid MenuPatch menuPatch) {
        return new Menu(1L, menuPatch.status(), LocalDateTime.now());
    }

    @GET
    @Path("/me/items")
    public List<MenuItem> listMenuItems() {
        return List.of(stubMenuItem(1L), stubMenuItem(2L));
    }

    @POST
    @Path("/me/items")
    public MenuItem addMenuItem(@Valid MenuItem menuItem) {
        return stubMenuItem(3L);
    }

    @PATCH
    @Path("/me/items/{itemId}")
    public MenuItem updateMenuItem(
            @PathParam("itemId") Long itemId,
            @Valid MenuItemPatch menuItemPatch
    ) {
        return stubMenuItem(itemId);
    }

    @DELETE
    @Path("/me/items/{itemId}")
    public void deleteMenuItem(@PathParam("itemId") Long itemId) {
    }

    private MenuItem stubMenuItem(Long id) {
        return new MenuItem(
                id,
                1L,
                "Tacos Signature " + id,
                new BigDecimal("12.90"),
                id.intValue() * 100,
                Boolean.TRUE
        );
    }
}
