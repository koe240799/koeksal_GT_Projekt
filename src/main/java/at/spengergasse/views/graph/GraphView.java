package at.spengergasse.views.graph;

import at.spengergasse.model.Graph;
import at.spengergasse.service.GraphService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.InputStream;
import java.util.List;

@PageTitle("Graph")
@Route("")
public class GraphView extends VerticalLayout {

    private final Grid<List<Object>> grid = new Grid<>();
    private final GraphService service = new GraphService();

    public GraphView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("CSV Upload"));

        FileBuffer buffer = new FileBuffer();
        Upload upload = new Upload(buffer);

        upload.setAcceptedFileTypes(".csv");

        upload.addSucceededListener(event -> {
            try {
                InputStream inputStream = buffer.getInputStream();

                Graph graph = service.parseCsvQuadratic(inputStream);

                getUI().ifPresent(ui ->
                        ui.access(() -> showGraph(graph))
                );

                Notification.show("Graph wurde erfolgreich geladen");

            } catch (Exception e) {
                Notification.show(e.getMessage());
            }
        });

        add(upload, grid);
    }

    // 🔥 BLOCKER + VIEW LOGIC
    private void showGraph(Graph graph) {

        grid.removeAllColumns();

        List<List<Object>> matrix = graph.getMatrix();

        if (matrix.isEmpty()) {
            grid.setItems(List.of());
            return;
        }


//        Wenn die Matrix nicht quadratisch ist dann wird kommt eine Fehlermeldung und es wird gestoppt!
        if (!isQuadratic(matrix)) {

            grid.setItems(matrix); // optional nur anzeigen

            Notification.show(
                    "⚠ Matrix ist NICHT quadratisch – Verarbeitung gestoppt",
                    5000,
                    Notification.Position.MIDDLE
            );

            return;
        }

        // Grid wird nur aufgebaut wenn die Matrix quadratisch ist!
        grid.addColumn(row ->
                        "Row " + matrix.indexOf(row)
                ).setHeader("#")
                .setFlexGrow(0)
                .setWidth("90px");

        int cols = matrix.get(0).size();

        for (int i = 0; i < cols; i++) {
            final int col = i;

            grid.addColumn(row ->
                    col < row.size() ? String.valueOf(row.get(col)) : "⚠"
            ).setHeader(toColumnName(i));
        }

        grid.setItems(matrix);
    }


    private boolean isQuadratic(List<List<Object>> matrix) {

        int size = matrix.get(0).size();

        for (List<Object> row : matrix) {
            if (row.size() != size) {
                return false;
            }
        }

        return matrix.size() == size;
    }


    private String toColumnName(int index) {
        StringBuilder sb = new StringBuilder();

        while (index >= 0) {
            sb.insert(0, (char) ('A' + (index % 26)));
            index = (index / 26) - 1;
        }

        return sb.toString();
    }
}