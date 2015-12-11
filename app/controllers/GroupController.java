package controllers;

import db.ArtifactDAO;
import db.GroupsDAO;
import models.Group;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard;
import views.html.groups;

public class GroupController extends Controller {
    public static Result GO_GROUPS = redirect(
            controllers.routes.GroupController.groups()
    );

    public static Result GO_DASHBOARD = redirect(
            controllers.routes.Application.dashboard()
    );

    @Security.Authenticated(Secured.class)
    public static Result groups() {
        ArtifactDAO db = ArtifactDAO.getInstance();

        User loggedInUser = User.findByEmail(request().username());
        return ok(groups.render(loggedInUser, false, null, GroupsDAO.allGroups()));
    }

    @Security.Authenticated(Secured.class)
    public static Result newGroup() {
        DynamicForm form = Form.form().bindFromRequest();

        String name, description;
        ArtifactDAO db = ArtifactDAO.getInstance();
        User loggedInUser = User.findByEmail(request().username());

        if (form.data().size() == 0) {
            return badRequest(dashboard.render(loggedInUser, true, "No data", db.timeSeriesList(), GroupsDAO.allGroups(), false, null));
        }
        name = form.get("name");
        description = form.get("desc");
        Group group = new Group(loggedInUser.id, name);
        if (!GroupsDAO.groupExists(group)) {
            GroupsDAO.insertGroup(new Group(loggedInUser.id, name, description));
            return GO_DASHBOARD;
        } else {
            //
            return badRequest(dashboard.render(loggedInUser, true, "Existing group", db.timeSeriesList(), GroupsDAO.allGroups(), false, null));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result updateGroup() {
        DynamicForm form = Form.form().bindFromRequest();

        String name, description;
        ArtifactDAO db = ArtifactDAO.getInstance();
        User loggedInUser = User.findByEmail(request().username());

        if (form.data().size() == 0) {
            return badRequest(dashboard.render(loggedInUser, true, "No group information", db.timeSeriesList(), GroupsDAO.allGroups(), false, null));
        }
        name = form.get("name");
        description = form.get("desc");
        System.out.println(name);
        Group oldGroup = new Group(loggedInUser.id, name);
        if (GroupsDAO.groupExists(oldGroup)) {
            GroupsDAO.updateGroup(oldGroup, new Group(loggedInUser.id, name, description));
            return GO_DASHBOARD;
        } else {
            //
            return badRequest(dashboard.render(loggedInUser, true, "Non Existing group", db.timeSeriesList(), GroupsDAO.allGroups(), false, null));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result removeGroup(String name) {
        ArtifactDAO db = ArtifactDAO.getInstance();
        User loggedInUser = User.findByEmail(request().username());

        Group group = new Group(loggedInUser.id, name);
        if (GroupsDAO.groupExists(group)) {
            GroupsDAO.deleteGroup(group);
            return GO_DASHBOARD;
        } else {
            //
            return badRequest(dashboard.render(loggedInUser, true, "Non Existing group", db.timeSeriesList(), GroupsDAO.allGroups(), false, null));
        }
    }
}
