package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class AnlysisActivity extends Activity {

    private LineChart line;
    private List<Entry> listLine=new ArrayList<>();          //实例化一个 List  用来保存你的数据
    private PieChart pie;
    private List<PieEntry> listPie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anlysis);
        line = (LineChart) findViewById(R.id.line);

//        list.add(new Entry(0,0));     //其中两个数字对应的分别是   X轴   Y轴
        listLine.add(new Entry(0,7));     //其中两个数字对应的分别是   X轴   Y轴
        listLine.add(new Entry(1,10));
        listLine.add(new Entry(2,12));
        listLine.add(new Entry(3,6));
        listLine.add(new Entry(4,3));

        LineDataSet lineDataSet=new LineDataSet(listLine,"语文");   //list是你这条线的数据  "语文" 是你对这条线的描述
        LineData lineData=new LineData(lineDataSet);
        line.setData(lineData);


        //折线图背景
//        line.setBackgroundColor(0x30000000);   //背景颜色
//        line.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        line.getAxisLeft().setDrawGridLines(true);  //是否绘制Y轴上的网格线（背景里面的横线）

        //对于右下角一串字母的操作
        line.getDescription().setEnabled(false);                  //是否显示右下角描述
//        line.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
//        line.getDescription().setTextSize(20);                    //字体大小
//        line.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legend=line.getLegend();
        legend.setEnabled(false);    //是否显示图例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置

        //X轴
        XAxis xAxis=line.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
//        xAxis.setAxisLineColor(Color.RED);   //X轴颜色
//        xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                for (int a=0;a<11;a++){     //用个for循环方便
                    if (a==v){
                        return a+1+"月";
                    }
                }
                return "";//注意这里需要改成 ""
            }
        });
        xAxis.setAxisMaximum(4);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(4,false);


        //Y轴
        YAxis AxisLeft=line.getAxisLeft();
        AxisLeft.setDrawGridLines(true);  //是否绘制Y轴上的网格线（背景里面的横线）
//        AxisLeft.setAxisLineColor(Color.BLUE);  //Y轴颜色
//        AxisLeft.setAxisLineWidth(2);           //Y轴粗细
        AxisLeft.setValueFormatter(new IAxisValueFormatter() {  //Y轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v!=0){
                    return Integer.valueOf((int)v).toString();
                }
                return "";//注意这里需要改成 ""
            }
        });
//        AxisLeft.setAxisMaximum(15);   //Y轴最大数值
//        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
//        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
//        AxisLeft.setLabelCount(15,false);

        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        line.getAxisRight().setEnabled(false);


        //折线
        //设置折线的式样   这个是圆滑的曲线（有好几种自己试试）     默认是直线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        List<Integer> colorLine = new ArrayList<>();
        colorLine.add(Color.rgb(67, 173, 109));
        lineDataSet.setColors(colorLine);  //折线的颜色
        lineDataSet.setLineWidth(2);        //折线的粗细
        //是否画折线点上的空心圆  false表示直接画成实心圆
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleHoleRadius(3);  //空心圆的圆心半径
        //圆点的颜色     可以实现超过某个值定义成某个颜色的功能   这里先不讲 后面单独写一篇
        colorLine = new ArrayList<>();
        colorLine.add(Color.rgb(253, 208, 81));
        lineDataSet.setCircleColor(Color.GRAY);
        lineDataSet.setCircleRadius(3);      //圆点的半径
        //定义折线上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return Integer.valueOf((int)v).toString()+"次";
                }
                return "";
            }
        });

        //数据更新
        line.notifyDataSetChanged();
        line.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        line.animateY(3000); //折线在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画





        pie = (PieChart) findViewById(R.id.pie);
        listPie=new ArrayList<>();


        listPie.add(new PieEntry(5,"今天是最糟糕的一天"));
        listPie.add(new PieEntry(6,"这是糟糕的一天"));
        listPie.add(new PieEntry(8,"这是正常的一天"));
        listPie.add(new PieEntry(12,"今天真好"));
        listPie.add(new PieEntry(3,"今天是最好的一天"));

        PieDataSet pieDataSet=new PieDataSet(listPie,"");
        PieData pieData=new PieData(pieDataSet);
        pie.setData(pieData);

        pie.setBackgroundColor(Color.WHITE);

        colorLine = new ArrayList<>();
        colorLine.add(Color.rgb(255, 113, 112));
        colorLine.add(Color.rgb(253, 162, 32));
        colorLine.add(Color.rgb(253, 208, 81));
        colorLine.add(Color.rgb(115, 212, 244));
        colorLine.add(Color.rgb(107, 208, 152));
        //设置各个数据的颜色
        pieDataSet.setColors(colorLine);
        //实体扇形的空心圆的半径   设置成0时就是一个圆 而不是一个环
        pie.setHoleRadius(30);
        //中间半透明白色圆的半径    设置成0时就是隐藏
        pie.setTransparentCircleRadius(30);
        //设置中心圆的颜色
        pie.setHoleColor(Color.CYAN);
        //设置中心部分的字  （一般中间白色圆不隐藏的情况下才设置）
        pie.setCenterText("心情占比");
        //设置中心字的字体颜色
        pie.setCenterTextColor(Color.GRAY);
        //设置中心字的字体大小
        pie.setCenterTextSize(16);
        //设置描述的字体大小（图中的  男性  女性）
        pie.setEntryLabelTextSize(20);
        //设置数据的字体大小  （图中的  44     56）
        pieDataSet.setValueTextSize(20);
        //设置描述的位置
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.6f);//设置描述连接线长度
        //设置数据的位置
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart2Length(0.6f);//设置数据连接线长度
        //设置两根连接线的颜色
        pieDataSet.setValueLineColor(Color.WHITE);

        //对于右下角一串字母的操作
        pie.getDescription().setEnabled(false);                  //是否显示右下角描述
        pie.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        pie.getDescription().setTextSize(20);                    //字体大小
        pie.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legends=pie.getLegend();
        legends.setEnabled(true);    //是否显示图例
        legends.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置


        //数据更新
        pie.notifyDataSetChanged();
        pie.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        pie.animateY(3000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画


    }
}