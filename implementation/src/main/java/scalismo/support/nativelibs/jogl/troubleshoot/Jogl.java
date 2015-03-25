package scalismo.support.nativelibs.jogl.troubleshoot;

import com.jogamp.opengl.util.FPSAnimator;
import scalismo.support.nativelibs.jogl.troubleshoot.gl3.GL3Sample;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class Jogl implements GLEventListener {

    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final int FRAME_PADDING = 50;
    private double theta = 0;
    private double s = 0;
    private double c = 0;

    public Jogl(String profileName, GLProfile glp, int x, int y) {
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("[GL2]" + profileName);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocation(x, y);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public static void main(String[] args) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Map<String, GLProfile> profiles = GLProfiles.getAvailableProfiles(true);

        int x = 0, y = 0;
        for (Map.Entry<String, GLProfile> entry : profiles.entrySet()) {
            GLProfile profile = entry.getValue();
            boolean showing = false;
            if (profile.isGL3()) {
                new GL3Sample(entry.getKey(), profile, x, y, FRAME_WIDTH, FRAME_HEIGHT);
                showing = true;
                System.out.println(entry.getKey()+ ": GL3");
            } else if (profile.isGL2()) {
                new Jogl(entry.getKey(), profile, x, y);
                showing = true;
                System.out.println(entry.getKey()+ ": GL2");
            } else {
                System.out.println(entry.getKey()+ ": neither GL2 nor GL3");
            }
            if (showing) {
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
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    private void update() {
        theta += 0.01;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw a triangle filling the window
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-c, -c);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(0, c);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(s, -s);
        gl.glEnd();
    }
}

