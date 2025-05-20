import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { adminService } from '../../services/api';
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Tabs,
  Tab,
  Paper,
} from '@mui/material';
import UsersTab from './tabs/UsersTab';
import CoursesTab from './tabs/CoursesTab';
import TermsTab from './tabs/TermsTab';
import ClassroomsTab from './tabs/ClassroomsTab';

function TabPanel({ children, value, index }) {
  return (
    <div hidden={value !== index}>
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

function AdminDashboard() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [dashboardData, setDashboardData] = useState(null);
  const [selectedTab, setSelectedTab] = useState(0);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        console.log('Fetching dashboard data...');
        const response = await adminService.getProfile();
        console.log('Dashboard data received:', response);
        setDashboardData(response.data);
        setError(null);
      } catch (err) {
        console.error('Error fetching dashboard data:', err);
        setError(err.message || 'Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Container>
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Admin Dashboard
      </Typography>

      <Paper sx={{ width: '100%', mb: 2 }}>
        <Tabs
          value={selectedTab}
          onChange={handleTabChange}
          indicatorColor="primary"
          textColor="primary"
          variant="scrollable"
          scrollButtons="auto"
        >
          <Tab label={`Users (${dashboardData?.students.length + dashboardData?.instructors.length + dashboardData?.employees.length})`} />
          <Tab label={`Courses (${dashboardData?.courses.length})`} />
          <Tab label={`Terms (${dashboardData?.terms.length})`} />
          <Tab label={`Classrooms (${dashboardData?.classrooms.length})`} />
        </Tabs>

        <TabPanel value={selectedTab} index={0}>
          <UsersTab
            students={dashboardData?.students || []}
            instructors={dashboardData?.instructors || []}
            employees={dashboardData?.employees || []}
          />
        </TabPanel>

        <TabPanel value={selectedTab} index={1}>
          <CoursesTab
            courses={dashboardData?.courses || []}
            courseSessions={dashboardData?.courseSessions || []}
          />
        </TabPanel>

        <TabPanel value={selectedTab} index={2}>
          <TermsTab terms={dashboardData?.terms || []} />
        </TabPanel>

        <TabPanel value={selectedTab} index={3}>
          <ClassroomsTab classrooms={dashboardData?.classrooms || []} />
        </TabPanel>
      </Paper>
    </Container>
  );
}

export default AdminDashboard; 