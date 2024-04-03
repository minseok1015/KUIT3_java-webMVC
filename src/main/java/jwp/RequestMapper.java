package jwp;

import jwp.controller.Controller;
import jwp.controller.HomeController;
import jwp.controller.ListUserController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private Map<String, Controller> controllerMap;

    public RequestMapper() {
        controllerMap = new HashMap<>();
        // 요청 경로와 컨트롤러 매핑
        controllerMap.put("/user/userList", new ListUserController());
        controllerMap.put("/", new HomeController());
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String mappingPath = uri.substring(contextPath.length());
        Controller controller = controllerMap.get(mappingPath);

        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested resource is not available.");
            return;
        }

        try {
            String viewName = controller.execute(req, resp);
            if (viewName.startsWith("redirect:")) {
                resp.sendRedirect(contextPath + viewName.substring("redirect:".length()));
            } else {
                req.getRequestDispatcher(viewName).forward(req, resp);
            }
        } catch (Exception e) {
        }
    }
}
