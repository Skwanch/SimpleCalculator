// Demonstrates JPanel, GridLayout and a few additional useful graphical features.
import java.awt.*;//Abstract Windows toolkit
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class SimpleCalc
{
	JFrame window;  // the main window which contains everything
	Container content ;
	JButton[] digits = new JButton[12]; 
	JButton[] ops = new JButton[4];
	JTextField expression;
	JButton equals;
	JTextField result;
	
	public SimpleCalc()
	{
		window = new JFrame( "Simple Calc");
		content = window.getContentPane();
		content.setLayout(new GridLayout(2,1)); // 2 row, 1 col
		ButtonListener listener = new ButtonListener();
		
		// top panel holds expression field, equals sign and result field  
		
		JPanel topPanel = new JPanel();//Jpanel = sub panel/mini Jframe
		topPanel.setLayout(new GridLayout(1,3)); // 1 row, 3 col
		
		expression = new JTextField();
		expression.setFont(new Font("verdana", Font.BOLD, 16));
		expression.setText("");
		
		equals = new JButton("=");
		equals.setFont(new Font("verdana", Font.BOLD, 20 ));
		equals.addActionListener( listener ); 
		
		result = new JTextField();
		result.setFont(new Font("verdana", Font.BOLD, 16));
		result.setText("");
		
		topPanel.add(expression);
		topPanel.add(equals);
		topPanel.add(result);
						
		// bottom panel holds the digit buttons in the left sub panel and the operators in the right sub panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2)); // 1 row, 2 col
	
		JPanel  digitsPanel = new JPanel();
		digitsPanel.setLayout(new GridLayout(4,3));	
		
		for (int i=0 ; i<10 ; i++ )
		{
			digits[i] = new JButton( ""+i );
			digitsPanel.add( digits[i] );
			digits[i].addActionListener( listener ); 
		}
		digits[10] = new JButton( "C" );
		digitsPanel.add( digits[10] );
		digits[10].addActionListener( listener ); 

		digits[11] = new JButton( "CE" );
		digitsPanel.add( digits[11] );
		digits[11].addActionListener( listener ); 		
	
		JPanel opsPanel = new JPanel();
		opsPanel.setLayout(new GridLayout(4,1));
		String[] opCodes = { "+", "-", "*", "/" };
		for (int i=0 ; i<4 ; i++ )
		{
			ops[i] = new JButton( opCodes[i] );
			opsPanel.add( ops[i] );
			ops[i].addActionListener( listener ); 
		}
		bottomPanel.add( digitsPanel );
		bottomPanel.add( opsPanel );
		
		content.add( topPanel );
		content.add( bottomPanel );
	
		window.setSize( 640,480);
		window.setVisible( true );
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Component whichButton = (Component) e.getSource();
			
			for (int i=0 ; i<10 ; i++ )
				if (whichButton == digits[i])
				{
					expression.setText( expression.getText() + i );
					return;
				}	
			if (whichButton == ops[0])//addition
			{
				expression.setText(expression.getText() + "+");
			}
			if (whichButton == ops[1])//subtraction
			{
				expression.setText(expression.getText() + "-");
			}
			if (whichButton == ops[2])//multiplication
			{
				expression.setText(expression.getText() + "*");
			}
			if (whichButton == ops[3])//division
			{
				expression.setText(expression.getText() + "/");
			}
			if (whichButton == digits[10])// C
			{
				expression.setText("");
				result.setText("");
			}
			if (whichButton == digits[11])// CE
			{
				result.setText("");
				String exp = expression.getText();
				if (exp.length() > 0)
				{
					exp = exp.substring(0, exp.length()-1);
					expression.setText(exp);
				}	
				else
				expression.setText("");

			}
			if (whichButton == equals)
			{
				int divideByZero = 0;//FLAG IF USER TRIES TO DIVIDE BY ZERO 
				int incompleteExp = 0;//FLAG IF EXPRESSION IS INCOMPLETE (EX: "1 + _" OR "1 * 1 * _")
				String exp = expression.getText();
				ArrayList<String> operators = new ArrayList<String>();
				ArrayList<String> operands = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer( exp,"+-*/", true );
				while (st.hasMoreTokens())
				{
					String token = st.nextToken();
					if ("+-/*".contains(token))
						operators.add(token);
					else
						operands.add(token);
				}
				for (int i = 0; i < operators.size(); i++)
				{
					if (operands.size() == operators.size()+1)
					{
						String op = operators.get(i);
					
						double x = Double.parseDouble(operands.get(i));
						double y = Double.parseDouble(operands.get(i+1));
						
						String res;
						
						if (op.equals("*"))
						{
							res = String.valueOf(x * y);
							operands.set(i, res);
							operands.remove(i+1);
							operators.remove(i);
							i--;
						}
				
						else if (op.equals("/"))
						{	
							if (y == 0)
							{
								divideByZero = 1;
								result.setText("ERROR");
								break;
							}
							res = String.valueOf(x / y);
							operands.set(i, res);
							operands.remove(i+1);
							operators.remove(i);
							i--;
						}
						
					}
					else 
					{
						incompleteExp = 1;
						break;
					}
				}
				
				for (int i = 0; i < operators.size(); i++)
				{
					if (operands.size() == operators.size()+1)
					{
						String op = operators.get(i);
						
						double x = Double.parseDouble(operands.get(i));
						double y = Double.parseDouble(operands.get(i+1));
						
						String res;
					
						if (op.equals("+"))
						{
							res = String.valueOf(x + y);
							operands.set(i, res);
							operands.remove(i+1);
							operators.remove(i);
							i--;
						}
						else if (op.equals("-"))
						{
							res = String.valueOf(x - y);
							operands.set(i, res);
							operands.remove(i+1);
							operators.remove(i);
							i--;
						}
					}
					else 
					{
						incompleteExp = 1;
						break;
					}
				}
				
				if (operands.size() > 0 && divideByZero == 0 && incompleteExp == 0)
				{ 
					result.setText(operands.get(0));
				}
				else if (divideByZero == 1 || incompleteExp == 1)
				{
					result.setText("ERROR");
				}
				else
				{
					result.setText("");
				}
			}
		}
	}
	public static void main(String [] args)
	{
		new SimpleCalc();
	}
}


