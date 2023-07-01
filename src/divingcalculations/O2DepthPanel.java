package divingcalculations;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class O2DepthPanel extends JPanel {


    private int o2;
    private int padding = 10;

    public O2DepthPanel(int o2) {
        this.o2 = o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2d = (Graphics2D) g;

        // 平滑绘制 （ 反锯齿 )
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int r = Math.round((width - 2 * padding) / 2f);

        int totalLength = Math.round((height - 3 * r - 5 * padding));
        int offset = Math.round(totalLength / 100f * (100 - o2));


        Rectangle r1 = new Rectangle(padding, r, 2 * r, 2 * r);
        Rectangle r2 = new Rectangle(padding, 2 * r + totalLength, 2 * r, 2 * r);

        // ar1 : 左侧圆弧               ar2 : 右侧圆弧
        Arc2D arc1 = new Arc2D.Double(r1, 0, 180, Arc2D.OPEN);
        Arc2D arc2 = new Arc2D.Double(r2, 180, 180, Arc2D.OPEN);

        // 构造一条闭合路径
        Path2D topShape = new Path2D.Double();
        Path2D bottomShape = new Path2D.Double();
        topShape.append(arc1, true);
        Line2D line = new Line2D.Double(new Point2D.Double(padding, 2 * r + offset),
                new Point2D.Double(padding + 2 * r, 2 * r + offset));
        topShape.append(line, true);
        topShape.closePath(); // 闭合 ( 自动连接最后一个点 和 最开始的点 ）

        g2d.setPaint(Color.BLACK);
        g2d.draw(topShape);
        g2d.setPaint(Color.YELLOW);
        g2d.fill(topShape);

        Line2D line2 = new Line2D.Double(new Point2D.Double(padding + 2 * r, 2 * r + offset),
                new Point2D.Double(padding, 2 * r + offset));
        bottomShape.append(line2, true);
        bottomShape.append(arc2, true);
        bottomShape.closePath();

        g2d.setPaint(Color.BLACK);
        g2d.draw(bottomShape);
        g2d.setPaint(Color.GREEN);
        g2d.fill(bottomShape);

        //draw text 76% N2 ，24% O2
        String n2Text = (100 - o2) + "% N2";
        String o2Text = o2 + "% O2";
        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("Default", Font.PLAIN, 15));
        FontMetrics fm = g.getFontMetrics(g2d.getFont()); // 创建一个FontMetrics对象
        int textWidthN2 = fm.stringWidth(n2Text);
        int textWidthO2 = fm.stringWidth(o2Text);
        int fontSize = fm.getHeight();
        g2d.drawString(n2Text, padding + r - textWidthN2 / 2, 2 * r + offset / 2f - fontSize / 2f);

        g2d.drawString(o2Text, padding + r - textWidthO2 / 2, 2 * r  + totalLength - fontSize / 2f);

    }

}
