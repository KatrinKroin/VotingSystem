   M S B A M L   `   `   `  ����G  CVotingSystem, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:ScottLogic.Controls.PieChartScottLogic.Controls.PieChart  5      )    #ScottLogic.Controls.PieChart.Legend   5      W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      8c*clr-namespace:ScottLogic.Controls.PieChart     5      $	��300��5      $	��300��6     � 	Resources  5      2   ,ScottLogic.Controls.PieChart.LegendConverter   legendConverter2   ,ScottLogic.Controls.PieChart.ColourConverter  colourConverter �� 
TargetType �� Property �� Value �� 
TargetType ��Name J� SnapsToDevicePixels �� Triggers P� Property	 P� Value
 �� 
TargetName �� Color��%�  &	        &	      '�� &       5   
   6     5	   
   6   �� 5   
   " ��6   .�5      ��  � �� 5      �� 5      " ��6&   .R�5      �� $ border��6!   
������ $��1q�6K   ._�5      �� # z 6/   #��z��6k   #��z��6�   5       5      P� 5       �@��
IsSelected	 . . �5   "   �� $
 border��6@    � �� 5   &   �� 5   &   $ Black��66   5   #   5      5      5      5      5       .��5"      � .+�5#   
   ��  �� Orientation$ Vertical=�6   .+�5%      �� -   5        $��legend��5%   �   $	��286��6   $��Stretch=�6#   
������ $��0q�6a   $ �� IsSynchronizedWithCurrentItem$
 TrueY�6u   $��0,30,0,0q�6�   ��6�   �� ��5&      �� 5'      .R�5(      � $��Stretch=�6   $��3q�6=   ��5)      �� 5*   "   $ Transparent��62    � ColumnDefinitions 5,      �� 5-   "   $��20 �63   �� 5.   "   $	��120 �63   �� 5/   "    � RowDefinitions 51      �� 52   "   .+�56      �� $��0��6(   $��13��68   $��13��6C   
 � Tag 6O   �� ��57   (   ��  �� RelativeSource �� *Self+ �� 	Converter# [ �� 5:      $��1��6(   $��3q�68   
�������$
��Bold"�6m   ��6C   �� *Class+6m   �� 5=      $��2��6(   $��3q�68    6C   �� ��5>   $   ��  �� *Self+# [  5?      5@      5B      5C      5D      5E      