/**
 * NeoDeploy Dashboard JavaScript
 * ========================================
 * 
 * This script handles:
 * - Fetching live data from the API
 * - Updating dashboard UI in real-time
 * - Triggering deployments
 * - Showing toast notifications
 * - Auto-refresh every 5 seconds
 */

// API Base URL
const API_BASE = '/api';

// Current deployment being tracked
let currentBuildId = null;
let refreshInterval = null;

// ========================================
// INITIALIZATION
// ========================================
document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 NeoDeploy Dashboard Loaded');
    
    // Initial load
    refreshDashboard();
    
    // Auto-refresh every 5 seconds
    refreshInterval = setInterval(refreshDashboard, 5000);
});

// ========================================
// API FUNCTIONS
// ========================================

/**
 * Fetch dashboard stats and update UI
 */
async function refreshDashboard() {
    try {
        // Fetch health status
        const healthResponse = await fetch(`${API_BASE}/health`);
        const health = await healthResponse.json();
        updateHealthStatus(health);
        
        // Fetch dashboard stats
        const dashResponse = await fetch(`${API_BASE}/dashboard`);
        const stats = await dashResponse.json();
        updateStats(stats);
        
        // Fetch deployment history
        const logsResponse = await fetch(`${API_BASE}/logs`);
        const logs = await logsResponse.json();
        updateHistory(logs);
        
        // If tracking a deployment, update its status
        if (currentBuildId) {
            const statusResponse = await fetch(`${API_BASE}/status/${currentBuildId}`);
            if (statusResponse.ok) {
                const deploy = await statusResponse.json();
                updateCurrentDeploy(deploy);
            }
        }
        
    } catch (error) {
        console.error('Failed to refresh dashboard:', error);
        updateHealthStatus({ status: 'DOWN' });
    }
}

/**
 * Trigger a new deployment
 */
async function triggerDeploy() {
    try {
        showToast('Triggering deployment...', 'info');
        
        const response = await fetch(`${API_BASE}/deploy?triggeredBy=dashboard`, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.success) {
            currentBuildId = result.buildId;
            showToast(`Deployment started: ${result.buildId}`, 'success');
            
            // Show current deploy section
            document.getElementById('currentDeploySection').style.display = 'block';
            document.getElementById('currentBuildId').textContent = result.buildId;
            
            // Start tracking
            trackDeployment(result.buildId);
        } else {
            showToast('Failed to trigger deployment', 'error');
        }
        
    } catch (error) {
        console.error('Deploy failed:', error);
        showToast('Deployment failed: ' + error.message, 'error');
    }
}

/**
 * Track deployment progress
 */
async function trackDeployment(buildId) {
    const checkStatus = async () => {
        try {
            const response = await fetch(`${API_BASE}/status/${buildId}`);
            const deploy = await response.json();
            
            updateCurrentDeploy(deploy);
            
            // Continue tracking if not done
            if (!['SUCCESS', 'FAILED'].includes(deploy.status)) {
                setTimeout(checkStatus, 1000);
            } else {
                // Deployment complete
                if (deploy.status === 'SUCCESS') {
                    showToast('Deployment successful! 🎉', 'success');
                } else {
                    showToast('Deployment failed ❌', 'error');
                }
                
                // Hide current deploy section after 5 seconds
                setTimeout(() => {
                    document.getElementById('currentDeploySection').style.display = 'none';
                    currentBuildId = null;
                }, 5000);
                
                // Refresh dashboard
                refreshDashboard();
            }
            
        } catch (error) {
            console.error('Status check failed:', error);
        }
    };
    
    checkStatus();
}

// ========================================
// UI UPDATE FUNCTIONS
// ========================================

/**
 * Update health status indicator
 */
function updateHealthStatus(health) {
    const statusEl = document.getElementById('appStatus');
    const statusText = statusEl.querySelector('.status-text');
    
    if (health.status === 'UP') {
        statusEl.className = 'status-indicator up';
        statusText.textContent = 'Online';
    } else {
        statusEl.className = 'status-indicator down';
        statusText.textContent = 'Offline';
    }
}

/**
 * Update statistics cards
 */
function updateStats(stats) {
    document.getElementById('totalDeploys').textContent = stats.totalDeploys || 0;
    document.getElementById('successCount').textContent = stats.successCount || 0;
    document.getElementById('failedCount').textContent = stats.failedCount || 0;
    document.getElementById('successRate').textContent = (stats.successRate || 100) + '%';
    document.getElementById('uptime').textContent = stats.uptime || '00:00:00';
}

/**
 * Update current deployment progress
 */
function updateCurrentDeploy(deploy) {
    document.getElementById('currentBuildId').textContent = deploy.buildId;
    
    const statusEl = document.getElementById('currentStatus');
    statusEl.textContent = deploy.status;
    statusEl.className = 'badge ' + deploy.status.toLowerCase();
    
    document.getElementById('currentMessage').textContent = deploy.message || '-';
    
    // Update progress bar and stages
    const stages = ['PENDING', 'BUILDING', 'TESTING', 'DEPLOYING', 'SUCCESS'];
    const stageIndex = stages.indexOf(deploy.status);
    const progress = ((stageIndex + 1) / stages.length) * 100;
    
    document.getElementById('progressFill').style.width = progress + '%';
    
    // Update stage indicators
    const stageIds = ['pending', 'building', 'testing', 'deploying', 'done'];
    stageIds.forEach((id, index) => {
        const stageEl = document.getElementById('stage-' + id);
        if (index < stageIndex) {
            stageEl.className = 'stage complete';
        } else if (index === stageIndex) {
            stageEl.className = 'stage active';
        } else {
            stageEl.className = 'stage';
        }
    });
    
    // Handle failed state
    if (deploy.status === 'FAILED') {
        document.getElementById('progressFill').style.background = 
            'linear-gradient(90deg, #ef4444, #dc2626)';
    }
}

/**
 * Update deployment history table
 */
function updateHistory(logs) {
    const tbody = document.getElementById('historyTableBody');
    
    if (!logs || logs.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="no-data">
                    No deployments yet. Click "Deploy Now" to start!
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = logs.map(log => `
        <tr>
            <td><code>${log.buildId}</code></td>
            <td><span class="badge ${log.status.toLowerCase()}">${log.status}</span></td>
            <td>${log.triggeredBy || 'unknown'}</td>
            <td>${formatDate(log.startTime)}</td>
            <td>${calculateDuration(log.startTime, log.endTime)}</td>
            <td>${log.message || '-'}</td>
        </tr>
    `).join('');
}

// ========================================
// UTILITY FUNCTIONS
// ========================================

/**
 * Format date for display
 */
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Calculate duration between two dates
 */
function calculateDuration(start, end) {
    if (!start) return '-';
    if (!end) return 'In progress...';
    
    const startDate = new Date(start);
    const endDate = new Date(end);
    const seconds = Math.round((endDate - startDate) / 1000);
    
    if (seconds < 60) return seconds + 's';
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}m ${remainingSeconds}s`;
}

/**
 * Show toast notification
 */
function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icon = {
        success: 'fa-check-circle',
        error: 'fa-times-circle',
        info: 'fa-info-circle'
    }[type] || 'fa-info-circle';
    
    toast.innerHTML = `
        <i class="fas ${icon}"></i>
        <span>${message}</span>
    `;
    
    container.appendChild(toast);
    
    // Remove after 4 seconds
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// ========================================
// KEYBOARD SHORTCUTS
// ========================================
document.addEventListener('keydown', (e) => {
    // Press 'D' to deploy
    if (e.key === 'd' && !e.ctrlKey && !e.metaKey) {
        // Don't trigger if typing in an input
        if (document.activeElement.tagName !== 'INPUT') {
            triggerDeploy();
        }
    }
    
    // Press 'R' to refresh
    if (e.key === 'r' && !e.ctrlKey && !e.metaKey) {
        if (document.activeElement.tagName !== 'INPUT') {
            refreshDashboard();
            showToast('Dashboard refreshed', 'info');
        }
    }
});
