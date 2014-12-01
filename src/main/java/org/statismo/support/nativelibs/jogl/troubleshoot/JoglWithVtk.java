package org.statismo.support.nativelibs.jogl.troubleshoot;


import org.statismo.support.nativelibs.NativeLibraryBundles;
import vtk.rendering.jogl.vtkAbstractJoglComponent;
import vtk.rendering.jogl.vtkJoglCanvasComponent;
import vtk.rendering.jogl.vtkJoglPanelComponent;
import vtk.rendering.vtkAbstractEventInterceptor;
import vtk.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class JoglWithVtk {

    public static void main(String[] args) throws Exception {
        NativeLibraryBundles.initialize(NativeLibraryBundles.InitializationMode.WARN_VERBOSE);

        final boolean usePanel = args.length > 0;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // build VTK Pipeline: cone
                final vtkConeSource cone = new vtkConeSource();
                cone.SetResolution(8);
                cone.Update();

                // mapper
                final vtkPolyDataMapper coneMapper = new vtkPolyDataMapper();
                coneMapper.SetInputConnection(cone.GetOutputPort());

                // actor
                final vtkActor coneActor = new vtkActor();
                coneActor.SetMapper(coneMapper);

                // VTK rendering part
                final vtkAbstractJoglComponent<?> joglWidget = usePanel ? new vtkJoglPanelComponent() : new vtkJoglCanvasComponent();
                System.out.println("We are using " + joglWidget.getComponent().getClass().getName() + " for the rendering.");

                joglWidget.getRenderer().AddActor(coneActor);

                // Add orientation axes
                vtkAbstractJoglComponent.attachOrientationAxes(joglWidget);

                // Add Scalar bar widget
                vtkLookupTable lut = new vtkLookupTable();
                lut.SetHueRange(.66, 0);
                lut.Build();
                vtkScalarBarWidget scalarBar = new vtkScalarBarWidget();
                scalarBar.SetInteractor(joglWidget.getRenderWindowInteractor());

                scalarBar.GetScalarBarActor().SetTitle("Example");
                scalarBar.GetScalarBarActor().SetLookupTable(lut);
                scalarBar.GetScalarBarActor().SetOrientationToHorizontal();
                scalarBar.GetScalarBarActor().SetTextPositionToPrecedeScalarBar();
                vtkScalarBarRepresentation srep = (vtkScalarBarRepresentation) scalarBar.GetRepresentation();
                srep.SetPosition(0.5, 0.053796);
                srep.SetPosition2(0.33, 0.106455);
                //scalarBar.ProcessEventsOff();
                scalarBar.EnabledOn();
                scalarBar.RepositionableOn();

                // Add interactive 3D Widget
                final vtkBoxRepresentation representation = new vtkBoxRepresentation();
                representation.SetPlaceFactor(1.05);
                representation.PlaceWidget(cone.GetOutput().GetBounds());

                final vtkBoxWidget2 boxWidget = new vtkBoxWidget2();
                boxWidget.SetRepresentation(representation);
                boxWidget.SetInteractor(joglWidget.getRenderWindowInteractor());
                boxWidget.SetPriority(1);

                final Runnable callback = new Runnable() {
                    vtkTransform transform = new vtkTransform();

                    public void run() {
                        //vtkBoxRepresentation rep = (vtkBoxRepresentation) boxWidget.GetRepresentation();
                        representation.GetTransform(transform);
                        coneActor.SetUserTransform(transform);
                    }
                };

                // Bind widget
                boxWidget.AddObserver("InteractionEvent", callback, "run");
                representation.VisibilityOn(); //no idea
                representation.HandlesOn(); // no idea
                boxWidget.SetEnabled(1); // without this, nothing happens at all.
                boxWidget.SetMoveFacesEnabled(1); // no idea

                // Add cell picker
                final vtkCellPicker picker = new vtkCellPicker();
                Runnable pickerCallback = new Runnable() {
                    public void run() {
                        if (picker.GetCellId() != -1) {
                            vtkCell cell = picker.GetDataSet().GetCell(picker.GetCellId());
                            System.out.println("Pick cell: " + picker.GetCellId() + " - Bounds: " + Arrays.toString(cell.GetBounds()));
                        }
                    }
                };
                joglWidget.getRenderWindowInteractor().SetPicker(picker);
                picker.AddObserver("EndPickEvent", pickerCallback, "run");

                // Bind pick action to double-click
                joglWidget.getInteractorForwarder().setEventInterceptor(new vtkAbstractEventInterceptor() {

                    public boolean mouseClicked(MouseEvent e) {
                        // Request picking action on double-click
                        final double[] position = {e.getX(), joglWidget.getComponent().getHeight() - e.getY(), 0};
                        if (e.getClickCount() == 2) {
                            System.out.println("Click trigger the picking (" + position[0] + ", " + position[1] + ")");
                            picker.Pick(position, joglWidget.getRenderer());
                        }

                        // We let the InteractionStyle process the event anyway
                        return false;
                    }
                });

                // UI part
                JFrame frame = new JFrame("JOGL With VTK Test");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(joglWidget.getComponent(),
                        BorderLayout.CENTER);
                frame.setSize(640, 480);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                joglWidget.resetCamera();
                joglWidget.getComponent().requestFocus();

                // Add r:ResetCamera and q:Quit key binding
                joglWidget.getComponent().addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == 'r') {
                            joglWidget.resetCamera();
                        } else if (e.getKeyChar() == 'q') {
                            System.exit(0);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }
                });
            }
        });
    }
}