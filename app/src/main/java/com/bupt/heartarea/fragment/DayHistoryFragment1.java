package com.bupt.heartarea.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bupt.heartarea.R;
import com.bupt.heartarea.bean.HistoryDataItemBean;
import com.bupt.heartarea.utils.GlobalData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * Created by yuqing on 2017/3/16.
 */
public class DayHistoryFragment1 extends Fragment {

    private ComboLineColumnChartView mDayChart;
    private ComboLineColumnChartData data;

    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private final int numberOfPoints = 24 * 60;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    //    public final static String[] days = new String[]{
//            "1", "2", "3", "4", "5", "6", "7", "8",
//            "9", "10", "11", "12", "13", "14", "15", "16",
//            "17", "18", "19", "20", "21", "22", "23", "24",
//            "25", "26", "27", "28", "29", "30", "31"};
    private String[] minutes = new String[24 * 60];
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = false;
    private LineChartData mLineChartData;
    private ColumnChartData mColumnChartData;

    private List<HistoryDataItemBean> mHistoryDataItemList = new ArrayList<>(GlobalData.historyDataItemBeanList);
    private Axis mAxisY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_day1, container, false);
        mDayChart = (ComboLineColumnChartView) view.findViewById(R.id.chart_history_day1);

        mDayChart.setOnValueTouchListener(new ValueTouchListener());
        generateXText();
        generateXY();
//        generateValues();
        generateData();
        return view;
    }

//    private void generateValues() {
//        for (int i = 0; i < maxNumberOfLines; ++i) {
//            for (int j = 0; j < numberOfPoints; ++j) {
//                randomNumbersTab[i][j] = (float) Math.random() * 50f + 5;
//            }
//        }
//    }

    /**
     * 将 2:35  20:40等时间转化成x坐标
     *
     * @param hour
     * @param min
     * @return
     */
    private int calculateX(int hour, int min) {

        return hour * 60 + min;
    }

    /**
     * 将 2:35  20:40等时间转化成x坐标
     *
     * @param time
     * @return
     */
    private int calculateX(String time) {
        String[] time_array = time.split(":");
        if (time_array.length > 0) {
            return calculateX(Integer.parseInt(time_array[0]), Integer.parseInt(time_array[1]));
        } else {
            Log.e("calculateX()错误", "时间格式不对，应为10:10:10");
            return 0;

        }


    }

    private void generateXText() {

        for (int i = 0; i < 24 * 60; i++) {
            String hour = String.valueOf(i / 60);
            String minute = i % 60 < 10 ? "0" + String.valueOf(i % 60) : String.valueOf(i % 60);
            minutes[i] = hour + ":" + minute;
        }
    }

    private void reset() {
        numberOfLines = 1;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        hasLabels = false;
        isCubic = false;

    }

    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(mColumnChartData, mLineChartData);
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < minutes.length; i++) {
            axisValues.add(new AxisValue(i).setLabel(minutes[i]));
        }


        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            axisX.setHasLines(true);
            mAxisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                // 坐标轴标签
                axisX.setName("时间");
                switch (GlobalData.currenttype) {
                    case HEART_RATE:
                        mAxisY.setName("心率/bps");
                        break;
                    case BLOOD_OXYGEN:
                        mAxisY.setName("血氧/%");
                        break;
                    case PRESSURE:
                        mAxisY.setName("压力值");
                    case BLOOD_PRESSURE:
                        mAxisY.setName("mmHg");
                        break;
                }
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(mAxisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        mDayChart.setComboLineColumnChartData(data);
    }

//    private LineChartData generateLineData() {
//
//        List<Line> lines = new ArrayList<Line>();
//        for (int i = 0; i < numberOfLines; ++i) {
//
//            List<PointValue> values = new ArrayList<PointValue>();
//            for (int j = 0; j < numberOfPoints; ++j) {
//                values.add(new PointValue(j, randomNumbersTab[i][j]));
//            }
//
//            Line line = new Line(values);
//            line.setColor(ChartUtils.COLORS[i]);
//            line.setCubic(isCubic);
//            line.setHasLabels(hasLabels);
//            line.setHasLines(hasLines);
//            line.setHasPoints(hasPoints);
//            lines.add(line);
//        }
//
//        LineChartData lineChartData = new LineChartData(lines);
//
//        return lineChartData;
//
//    }
//
//    /**
//     * @return
//     */
//    private ColumnChartData generateColumnData() {
//        int numSubcolumns = 1;
//        int numColumns = numberOfPoints;
//        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
//        List<Column> columns = new ArrayList<Column>();
//        List<SubcolumnValue> values;
//        for (int i = 0; i < numColumns; ++i) {
//
//            values = new ArrayList<SubcolumnValue>();
//            for (int j = 0; j < numSubcolumns; ++j) {
//                float top = (float) Math.random() * 50 + 5;
//                float bottom = top - 15;
//                values.add(new SubcolumnValue(top, bottom, ChartUtils.COLOR_GREEN));
//            }
//
//            columns.add(new Column(values));
//        }
//
//        ColumnChartData columnChartData = new ColumnChartData(columns);
//        return columnChartData;
//    }
//
//    private void addLineToData() {
//        if (data.getLineChartData().getLines().size() >= maxNumberOfLines) {
//            Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            ++numberOfLines;
//        }
//
//        generateData();
//    }
//
//    private void toggleLines() {
//        hasLines = !hasLines;
//
//        generateData();
//    }
//
//    private void togglePoints() {
//        hasPoints = !hasPoints;
//
//        generateData();
//    }
//
//    private void toggleCubic() {
//        isCubic = !isCubic;
//
//        generateData();
//    }
//
//    private void toggleLabels() {
//        hasLabels = !hasLabels;
//
//        generateData();
//    }
//
//    private void toggleAxes() {
//        hasAxes = !hasAxes;
//
//        generateData();
//    }
//
//    private void toggleAxesNames() {
//        hasAxesNames = !hasAxesNames;
//
//        generateData();
//    }
//
//    private void prepareDataAnimation() {
//
//        // Line animations
//        for (Line line : data.getLineChartData().getLines()) {
//            for (PointValue value : line.getValues()) {
//                // Here I modify target only for Y values but it is OK to modify X targets as well.
//                value.setTarget(value.getX(), (float) Math.random() * 50 + 5);
//            }
//        }
//
//        // Columns animations
//        for (Column column : data.getColumnChartData().getColumns()) {
//            for (SubcolumnValue value : column.getValues()) {
//                value.setTarget((float) Math.random() * 50 + 5);
//            }
//        }
//    }

    private class ValueTouchListener implements ComboLineColumnChartOnValueSelectListener {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            String temp = GlobalData.select_date;
            String[] temp_array = temp.split("-");
            int month = Integer.valueOf(temp_array[1]);
            String time = minutes[columnIndex];
//            int day = Integer.valueOf(date);
            switch (GlobalData.currenttype) {
                case HEART_RATE:
                    Toast.makeText(getActivity(), "在" + month + "在" + time + "，您的心率最大值为"
                            + value.getValueTop() + ",最小值为" + value.getValueBottom(), Toast.LENGTH_SHORT).show();
                    break;
                case PRESSURE:
                    Toast.makeText(getActivity(), "在" + month + "在" + time + "，您的疲劳度最大值为"
                            + value.getValueTop() + ",最小值为" + value.getValueBottom(), Toast.LENGTH_SHORT).show();
                    break;
                case BLOOD_OXYGEN:
                    Toast.makeText(getActivity(), "在" + month + "在" + time + "，您的血氧最大值为"
                            + value.getValueTop() + ",最小值为" + value.getValueBottom(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
            String temp = GlobalData.select_date;
            String[] temp_array = temp.split("-");
            int month = Integer.valueOf(temp_array[1]);
            String time = minutes[(int) value.getX()];
//            int day = Integer.valueOf(date);
            switch (GlobalData.currenttype) {
                case HEART_RATE:
                    Toast.makeText(getActivity(), "在" + time + "，您的心率值为"
                            + value.getY(), Toast.LENGTH_SHORT).show();
                    break;
                case PRESSURE:
                    Toast.makeText(getActivity(), "在" + time + "，您的疲劳度值为"
                            + value.getY(), Toast.LENGTH_SHORT).show();
                    break;
                case BLOOD_OXYGEN:
                    Toast.makeText(getActivity(), "在" + time + "，您的血氧值为"
                            + value.getY(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }


//    /**
//     * 将日期转化成x坐标
//     *
//     * @param day
//     * @return
//     */
//    private int calculateX(int day) {
//
//        return day - 1;
//    }
//
//    /**
//     * 将日期转化成x坐标
//     *
//     * @param date
//     * @return
//     */
//    private int calculateX(String date) {
//        String[] date_array = date.split("-");
//
//        int day_int = Integer.parseInt(date_array[2]);
//        if (day_int >= 0) {
//            return calculateX(day_int);
//        } else {
//            Log.e("calculateX()错误", "日期格式不对");
//            return 0;
//
//        }
//
//
//    }

    /**
     * 将一个时间段区间的Y值list的平均值、最大值、最小值求出
     *
     * @param y_list
     * @return result[0]平均值，result[1]为最大值，result[2]为最小值
     */
    float[] calculateY(List<Float> y_list) {
        float[] result = {0, 0, 0};
        if (y_list.size() == 0 || y_list == null) return result;
        float sum = 0;
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < y_list.size(); i++) {
            float num = y_list.get(i);
            sum += num;
            if (max < num) max = num;
            if (min > num) min = num;
        }
        result[0] = sum / y_list.size();
        result[1] = max;
        result[2] = min;
        return result;

    }

    /**
     * 将一个时间段区间的Y值list的平均值、最大值、最小值求出
     *
     * @param y_list_high
     * @param y_list_low
     * @return result[0]平均值，result[1]为最大值，result[2]为最小值
     */
    float[] calculateYForBloodPressure(float y_list_high, float y_list_low) {
        float[] result = {0, 0, 0};
        result[1] = y_list_high;
        result[2] = y_list_low;
        return result;

    }

    /**
     * 将一个时间段区间的Y值数组的平均值、最大值、最小值求出
     *
     * @param y_array
     * @return result[0]平均值，result[1]为最大值
     */
    float[] calculateY(float[] y_array) {
        float[] result = {0, 0, 0};
        float sum = 0;
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < y_array.length; i++) {
            float num = y_array[i];
            sum += num;
            if (max < num) max = num;
            if (min > num) min = num;
        }
        result[0] = sum / y_array.length;
        result[1] = max;
        result[2] = min;
        return result;

    }

    /**
     * 计算出折线图和柱形图的数据集
     */
    private void generateXY() {
        List<List<Float>> listList = new ArrayList<>();
        for (int i = 0; i < minutes.length; i++) {
            List<Float> list = new ArrayList<>();
            listList.add(list);
        }
        // 为血压设的高压列表、低压列表
        List<Float> list_high = new ArrayList<>();
        for (int i = 0; i < minutes.length; i++) {
            list_high.add(0.0f);
        }

        List<Float> list_low = new ArrayList<>();
        for (int i = 0; i < minutes.length; i++) {
            list_low.add(0.0f);
        }


        for (HistoryDataItemBean item : mHistoryDataItemList) {
            switch (GlobalData.currenttype) {
                case HEART_RATE:
                    listList.get(calculateX(item.getTime())).add((float) item.getHeart_rate());
                    break;
                case BLOOD_OXYGEN:
                    listList.get(calculateX(item.getTime())).add((float) item.getBlood_oxygen());
                    break;
                case PRESSURE:
                    listList.get(calculateX(item.getTime())).add((float) item.getPressure());
                    break;
                // 血压
                case BLOOD_PRESSURE:
                    list_high.set(calculateX(item.getTime()), (float) item.getBlood_pressure_high());
                    list_low.set(calculateX(item.getTime()), (float) item.getBlood_pressure_low());
                    break;

            }


        }


        List<Line> lines = new ArrayList<Line>();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values1 = new ArrayList<PointValue>();
            List<SubcolumnValue> values2;

            for (int j = 0; j < numberOfPoints; ++j) {
                float[] resY = {0, 0, 0};
                if (GlobalData.currenttype == GlobalData.MeasureType.BLOOD_PRESSURE) // TODO: 17/7/5 是血压
                {
//                    resY = calculateYForBloodPressure(list_high.get(j), list_low.get(j));
                    resY[1] = list_high.get(j);
                    resY[2] = list_low.get(j);
                    Log.e("血压resY：" , resY[0] + " " + resY[1] + " " + resY[2]);
                } else {
                    // 平均值放入折线图数据集中
                    resY = calculateY(listList.get(j));
                    if (listList.get(j).size() > 0) {

                        values1.add(new PointValue(j, resY[0]));
                    }
                }

                values2 = new ArrayList<>();
                Log.e("当前类型：" + GlobalData.currenttype + "  resY", resY[0] + " " + resY[1] + " " + resY[2]);
                values2.add(new SubcolumnValue(resY[1], resY[2], getResources().getColor(R.color.column_chart_green)));
                columns.add(new Column(values2));
            }

            Line line = new Line(values1);
            line.setColor(getResources().getColor(R.color.linear_chart_green));
            line.setCubic(isCubic);
            line.setHasLabels(hasLabels);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        mLineChartData = new LineChartData(lines);
        mColumnChartData = new ColumnChartData(columns);
    }
}
