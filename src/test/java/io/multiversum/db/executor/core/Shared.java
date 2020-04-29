package io.multiversum.db.executor.core;

public class Shared {

	public static Object[][] Queries = {
		{"USE T1;"},
		{"SELECT Student_ID from STUDENT;"},
		{"SELECT * FROM STUDENT;"},
		{"SELECT EMP_ID, NAME FROM EMPLOYEE_TBL WHERE EMP_ID = '0000';"},
		{"SELECT EMP_ID, LAST_NAME FROM EMPLOYEE WHERE CITY = 'Seattle' ORDER BY EMP_ID;"},
		{"SELECT EMP_ID, LAST_NAME FROM EMPLOYEE_TBL WHERE CITY = 'INDIANAPOLIS' ORDER BY EMP_ID asc;"},
		{"SELECT Name, Age FROM Patients WHERE Age > 40 GROUP BY Name, Age ORDER BY Name;"},
		{"SELECT COUNT(price), price FROM orders WHERE price < 70 GROUP BY price ORDER BY price"},
		{"SELECT COUNT(CustomerID), Country FROM Customers GROUP BY Country;"},
		{"SELECT SUM(Salary) FROM Employee WHERE Emp_Age < 30;"},
		{"SELECT AVG(Price) FROM Products;"},
		{"SELECT MIN(Price) FROM Products;"},
		{"SELECT MAX(Price) FROM Products;"},
		{"UPDATE Customers SET Zip=Phone, Phone=Zip;"},
		{"SELECT DISTINCT ID FROM Customers;"},
		{"SELECT * FROM Customers WHERE Customer_ID<>NULL LIMIT 25;"},
		{"SELECT * FROM Customers WHERE Customer_ID<>NULL LIMIT 25 OFFSET 10;"},
		{"SELECT * From Customers WHERE Name LIKE 'Herb%';"},
		{"SELECT ID FROM Orders WHERE Date BETWEEN '01/12/2018' AND '01/13/2018'"},
		{"SELECT phone FROM Customers UNION SELECT item FROM Orders;"},
		{"SELECT Item AS item_description FROM Orders;"},
		{"SELECT Item FROM Orders WHERE id in (SELECT ID FROM Orders WHERE quantity > 50);"},
		{"CREATE TABLE Customers (ID varchar(80),Name varchar(80),Phone varchar(20));"},
		{"ALTER TABLE Customers ADD Birthday varchar(80)"},
		{"DROP TABLE table_name;"},
		{"SELECT Name FROM Customers WHERE EXISTS (SELECT Item FROM Orders WHERE Customers.ID = Orders.ID AND Price < 50);"},
		{"INSERT INTO Yearly_Orders SELECT * FROM Orders WHERE Date<='1/1/2018';"},
		{"SELECT Item, Price * (QtyInStock + IFNULL(QtyOnOrder, 0)) FROM Orders;"},
		{"SELECT COUNT(ID), Region FROM Customers GROUP BY Region HAVING COUNT(ID) > 0;"},
		{"SELECT SUBSTRING_INDEX('www.bytescout.com', '.', 2);"},
		{"SELECT COALESCE(NULL,NULL,'ByteScout',NULL,'Byte');"},
		{"SELECT CONVERT(int, 27.64);"},
	};
	
}
