import React, { useState } from 'react';
import {
  Box,
  Tabs,
  Tab,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Chip,
  Button,
} from '@mui/material';
import AddUserForm from '../components/AddUserForm';
import { adminService } from '../../../services/api';

function TabPanel({ children, value, index }) {
  return (
    <div hidden={value !== index}>
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

function UsersTab({ students, instructors, employees, onUserAdded }) {
  const [value, setValue] = useState(0);
  const [openAddUser, setOpenAddUser] = useState(false);

  const handleTabChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleAddUser = async (userData) => {
    try {
      let response;
      switch (userData.userType) {
        case 'STUDENT':
          response = await adminService.addStudent(userData);
          break;
        case 'INSTRUCTOR':
          response = await adminService.addInstructor(userData);
          break;
        case 'EMPLOYEE':
          response = await adminService.addEmployee(userData);
          break;
        default:
          throw new Error('Invalid user type');
      }
      
      if (response.data) {
        onUserAdded(response.data);
      }
    } catch (error) {
      console.error('Error adding user:', error);
      // You might want to show an error message to the user here
    }
  };

  const renderUserTable = (users, type) => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Username</TableCell>
            {type === 'student' && <TableCell>Term</TableCell>}
            {type === 'instructor' && <TableCell>Certification No</TableCell>}
            {type === 'employee' && <TableCell>Department</TableCell>}
          </TableRow>
        </TableHead>
        <TableBody>
          {users.map((user) => (
            <TableRow key={user.id}>
              <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
              <TableCell>{user.email}</TableCell>
              <TableCell>{user.username}</TableCell>
              {type === 'student' && <TableCell>{user.termName || 'N/A'}</TableCell>}
              {type === 'instructor' && <TableCell>{user.certificationNo || 'N/A'}</TableCell>}
              {type === 'employee' && <TableCell>{user.department || 'N/A'}</TableCell>}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h6">
          User Management
        </Typography>
        <Button
          variant="contained"
          color="primary"
          onClick={() => setOpenAddUser(true)}
        >
          Add New User
        </Button>
      </Box>

      <Tabs
        value={value}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="primary"
        sx={{ mb: 2 }}
      >
        <Tab label={`Students (${students.length})`} />
        <Tab label={`Instructors (${instructors.length})`} />
        <Tab label={`Employees (${employees.length})`} />
      </Tabs>

      <TabPanel value={value} index={0}>
        {renderUserTable(students, 'student')}
      </TabPanel>

      <TabPanel value={value} index={1}>
        {renderUserTable(instructors, 'instructor')}
      </TabPanel>

      <TabPanel value={value} index={2}>
        {renderUserTable(employees, 'employee')}
      </TabPanel>

      <AddUserForm
        open={openAddUser}
        onClose={() => setOpenAddUser(false)}
        onAdd={handleAddUser}
      />
    </Box>
  );
}

export default UsersTab; 