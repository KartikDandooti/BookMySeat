package com.valtech.team3.bookmyseatbackend.service;

import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;

public interface AdminDashboardService {

	/**
	 * Retrieves an instance of AdminDashboardModel.
	 * 
	 * This method is responsible for fetching the model representing the data and
	 * functionality related to the admin dashboard. The AdminDashboardModel
	 * typically encapsulates various data points and operations required for
	 * presenting and managing administrative dashboard information.
	 * 
	 * @return An instance of AdminDashboardModel containing the necessary data and
	 *         operations for the admin dashboard.
	 * 
	 * @see AdminDashboardModel
	 */
	AdminDashboardModel getAdminDashboardModel();
}