/*
 * Copyright 2016 University of Basel, Graphics and Vision Research Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalismo.support.nativelibs.jogl.troubleshoot;


import vtk.rendering.jogl.vtkAbstractJoglComponent;
import vtk.rendering.jogl.vtkJoglCanvasComponent;
import vtk.rendering.jogl.vtkJoglPanelComponent;
import vtk.rendering.vtkAbstractEventInterceptor;
import vtk.*;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;

public class JoglWithVtk {

    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final int FRAME_PADDING = 50;

    public static void main(String[] args) {

        boolean usePanel = args.length > 0;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Map<String, GLProfile> profiles = GLProfiles.getAvailableProfiles(true);

        int x = 0, y = 0;
        for (Map.Entry<String, GLProfile> entry : profiles.entrySet()) {
            showFrameForProfile(entry.getKey(), entry.getValue(), usePanel, x, y);
            x += (FRAME_WIDTH + FRAME_PADDING);
            if (x + FRAME_WIDTH > screenSize.width) {
                x = 0;
                y += FRAME_PADDING;
                if (y + FRAME_HEIGHT <= screenSize.height) {
                    y += FRAME_HEIGHT;
                }
            }
        }

    }

    private static void showFrameForProfile(final String profileName, GLProfile profile, final boolean usePanel, final int x, final int y) {
        final GLCapabilities capas = new GLCapabilities(profile);

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
                vtkGenericOpenGLRenderWindow window = new vtkGenericOpenGLRenderWindow();
                final vtkAbstractJoglComponent<?> joglWidget = usePanel ? new vtkJoglPanelComponent(window, capas) : new vtkJoglCanvasComponent(window, capas);
                //System.out.println("We are using " + joglWidget.getComponent().getClass().getName() + " for the rendering.");

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
                JFrame frame = new JFrame(profileName);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(joglWidget.getComponent(),
                        BorderLayout.CENTER);
                frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
                frame.setLocation(x, y);
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