package org.peiquan;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Calculator {

    private LinkedList<Step> undoList;
    private LinkedList<Step> redoList;
    // 当前步骤
    private Step currentStep;

    public Calculator() {
        undoList = new LinkedList<>();
        redoList = new LinkedList<>();
        currentStep = new Step(BigDecimal.ZERO,"+",BigDecimal.ZERO,BigDecimal.ZERO);
        undoList.push(currentStep);
    }

    public void undo() {
        if (undoList.isEmpty()) {
            System.out.println("undoList is empty, ignore undo operation");
            return;
        }
        Step step = undoList.pop();
        redoList.push(currentStep);
        System.out.println("执行undo操作 " + currentStep + "，恢复到 " + step + ",历史值：" + currentStep.value + "，新值：" + step.value);
        currentStep = step;
    }
    public void redo() {
        if (redoList.isEmpty()) {
            System.out.println("redoList is empty, ignore redo operation");
            return;
        }

        Step step = redoList.pop();
        undoList.push(currentStep);
        System.out.println("执行redo操作 " + currentStep + "，恢复到 " + step + ",历史值：" + currentStep.value + "，新值：" + step.value);
        currentStep = step;
    }

    public void add(String num) {
        calcul("+",num);
    }
    public void subtract(String num) {
        calcul("-",num);
    }
    public void multiply(String num) {
        calcul("*",num);
    }
    public void divide(String num) {
        calcul("/",num);
    }
    private void calcul(String operator,String num) {
        BigDecimal oldResult = currentStep == null ? BigDecimal.ZERO : currentStep.value;
        BigDecimal result;
        switch (operator) {
            case "+":
                result = oldResult.add(new BigDecimal(num));
                break;
            case "-":
                result = oldResult.subtract(new BigDecimal(num));
                break;
            case "*":
                result = oldResult.multiply(new BigDecimal(num));
                break;
            case "/":
                result = oldResult.divide(new BigDecimal(num));
                break;
            default:
                throw new IllegalArgumentException("invalid operator");
        }
        Step step = new Step(oldResult, operator,new BigDecimal(num), result);
        if (currentStep != null) {
            undoList.push(currentStep);
        }
        currentStep = step;
        System.out.println(step);
    }

    static class Step {
        //操作符：+、-、*、/
        private String operator;
        //第一个参数
        private BigDecimal num1;

        //第二个参数
        private BigDecimal num2;

        //操作结果
        private BigDecimal value;

        public Step() {

        }

        public Step(BigDecimal num1,String operator, BigDecimal num2, BigDecimal value) {
            this.operator = operator;
            this.num1 = num1;
            this.num2 = num2;
            this.value = value;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }


        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public BigDecimal getNum1() {
            return num1;
        }

        public void setNum1(BigDecimal num1) {
            this.num1 = num1;
        }

        public BigDecimal getNum2() {
            return num2;
        }

        public void setNum2(BigDecimal num2) {
            this.num2 = num2;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s = %s",num1, operator,num2,value);
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.add("1");
        calculator.add("2");
        calculator.subtract("3");
        calculator.redo();
        calculator.undo();
        calculator.redo();
        calculator.undo();
        calculator.undo();
        calculator.redo();
        calculator.subtract("2");
        calculator.multiply("2");
        calculator.divide("2");
    }
}