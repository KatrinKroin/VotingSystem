   M S B A M L   `   `   `  ���� G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:ScottLogic.Controls.PieChartScottLogic.Controls.PieChart  )    #ScottLogic.Controls.PieChart.Legend   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     8c*clr-namespace:ScottLogic.Controls.PieChart     $	��300��$	��300��  � 	Resources  2   ,ScottLogic.Controls.PieChart.LegendConverter   legendConverter2   ,ScottLogic.Controls.PieChart.ColourConverter  colourConverter �� 
TargetType �� Property �� Value �� 
TargetType ��Name J� SnapsToDevicePixels �� Triggers P� Property	 P� Value
 �� 
TargetName �� Color��%�   &	        &	      '�� 
         �� " ��.���  � �� �� " ��.R��� $ border��
������ $��1q�._��� # z #��z��#��z�� P�  �@��
IsSelected	 . . ��� $
 border�� � �� �� $ Black��.��� .+���  �� Orientation$ Vertical=�.+��� -   $��legend��$	��286��$��Stretch=�
������ $��0q�$ �� IsSynchronizedWithCurrentItem$
 TrueY�$��0,30,0,0q����� ���� .R�� $��Stretch=�$��3q����� $ Transparent�� � ColumnDefinitions �� $��20 ��� $	��120 ���  � RowDefinitions �� .+��� $��0��$��13��$��13��
 � Tag �� ����  �� RelativeSource �� *Self+ �� 	Converter# [ �� $��1��$��3q�
�������$
��Bold"����� *Class+�� $��2��$��3q� �� ����  �� *Self+# [  