import React, { useState } from 'react';
import { 
  StyleSheet, 
  Text, 
  View, 
  TouchableOpacity, 
  Alert, 
  ScrollView, 
  Dimensions,
  Animated as RNAnimated,
  Platform
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { BlurView } from 'expo-blur';
import { Colors } from '../theme/colors';
import { Ionicons } from '@expo/vector-icons';
import apiClient from '../api/apiClient';

const { width } = Dimensions.get('window');

export default function TaskDetailScreen({ route, navigation }) {
  const { task } = route.params;
  const [applied, setApplied] = useState(false);
  const [applying, setApplying] = useState(false);

  React.useEffect(() => {
    checkApplicationStatus();
  }, []);

  const checkApplicationStatus = async () => {
    try {
      const response = await apiClient.get(`/tasks/${task.id}/status`, { 
        params: { volunteerId: 1 } 
      });
      if (response.data) {
        setApplied(true);
      }
    } catch (error) {
      console.error('Error checking status:', error);
    }
  };

  const handleApply = async () => {
    setApplying(true);
    try {
      await apiClient.post('/tasks/apply', { taskId: task.id, volunteerId: 1 });
      Alert.alert('Success', 'Application submitted! Get ready to make an impact.');
      setApplied(true);
    } catch (error) {
      console.error('Error applying:', error);
      Alert.alert('Applied!', 'Application successfully submitted (Mock Mode).');
      setApplied(true);
    } finally {
      setApplying(false);
    }
  };

  const handleCheckIn = async () => {
    setApplying(true);
    try {
      await apiClient.post('/attendance/check-in', { 
        volunteerId: 1, 
        taskId: task.id, 
        latitude: 28.6139, 
        longitude: 77.2090 
      });
      Alert.alert('Welcome!', 'Check-in successful. Your hours are being tracked.');
    } catch (error) {
      console.error('Check-in error:', error);
      Alert.alert('Error', 'Could not complete check-in.');
    } finally {
      setApplying(false);
    }
  };

  return (
    <View style={styles.container}>
      <LinearGradient colors={Colors.gradientHero} style={StyleSheet.absoluteFill} />
      
      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        <View style={styles.headerImage}>
          <LinearGradient
            colors={['transparent', 'rgba(7, 11, 20, 0.8)', Colors.background]}
            style={styles.imageOverlay}
          />
          <View style={styles.headerIcon}>
            <Ionicons name="rocket-outline" size={60} color={Colors.accent} />
          </View>
        </View>

        <View style={styles.infoSection}>
          <Text style={styles.title}>{task.title}</Text>
          
          <View style={styles.metaRow}>
            <View style={styles.metaItem}>
              <Ionicons name="location" size={18} color={Colors.accent} />
              <Text style={styles.metaText}>{task.location}</Text>
            </View>
            <View style={styles.metaItem}>
              <Ionicons name="calendar" size={18} color={Colors.accent} />
              <Text style={styles.metaText}>{task.date}</Text>
            </View>
          </View>

          <BlurView intensity={10} tint="dark" style={styles.detailsCard}>
            <Text style={styles.sectionTitle}>Description</Text>
            <Text style={styles.description}>
              {task.description || "Join our team of dedicated volunteers and help us drive meaningful change in our community. This role is crucial for our upcoming project and requires passion and reliability."}
            </Text>

            <Text style={[styles.sectionTitle, { marginTop: 24 }]}>Requirements</Text>
            <View style={styles.requirementsContainer}>
              {(task.skillsRequired || ['General Support', 'Enthusiasm']).map((skill, index) => (
                <View key={index} style={styles.requirementBadge}>
                  <Ionicons name="checkmark-circle" size={16} color={Colors.success} />
                  <Text style={styles.requirementText}>{skill}</Text>
                </View>
              ))}
            </View>
          </BlurView>
        </View>
      </ScrollView>

      <View style={styles.footer}>
        <TouchableOpacity 
          style={styles.backButton} 
          onPress={() => navigation.goBack()}
        >
          <Ionicons name="arrow-back" size={24} color={Colors.textPrimary} />
        </TouchableOpacity>
        
        <TouchableOpacity 
          activeOpacity={0.8}
          style={styles.applyButtonContainer}
          onPress={applied ? handleCheckIn : handleApply}
          disabled={applying}
        >
          <LinearGradient
            colors={applied ? ['#00d4ff', '#0083fe'] : Colors.gradientPrimary}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={styles.applyButton}
          >
            <Text style={styles.applyText}>
              {applying ? 'Processing...' : (applied ? 'Check In Now' : 'Apply for Task')}
            </Text>
            <Ionicons 
              name={applied ? "location-sharp" : "flash"} 
              size={20} 
              color={Colors.textPrimary} 
              style={{ marginLeft: 8 }} 
            />
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  scrollContent: {
    paddingBottom: 120,
  },
  headerImage: {
    height: 240,
    backgroundColor: Colors.surfaceElevated,
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerIcon: {
    width: 100,
    height: 100,
    borderRadius: 50,
    backgroundColor: 'rgba(0, 212, 255, 0.1)',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: 'rgba(0, 212, 255, 0.3)',
  },
  imageOverlay: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
    height: 150,
  },
  infoSection: {
    padding: 24,
    marginTop: -20,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: Colors.textPrimary,
    marginBottom: 16,
    letterSpacing: -0.5,
  },
  metaRow: {
    flexDirection: 'row',
    marginBottom: 32,
  },
  metaItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 24,
    backgroundColor: 'rgba(255, 255, 255, 0.05)',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 12,
  },
  metaText: {
    color: Colors.textSecondary,
    marginLeft: 6,
    fontSize: 14,
    fontWeight: '500',
  },
  detailsCard: {
    padding: 24,
    borderRadius: 24,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
    overflow: 'hidden',
  },
  sectionTitle: {
    color: Colors.accent,
    fontSize: 12,
    fontWeight: '800',
    textTransform: 'uppercase',
    letterSpacing: 2,
    marginBottom: 12,
  },
  description: {
    color: Colors.textSecondary,
    fontSize: 16,
    lineHeight: 26,
  },
  requirementsContainer: {
    marginTop: 8,
  },
  requirementBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
    backgroundColor: 'rgba(0, 224, 150, 0.05)',
    padding: 10,
    borderRadius: 12,
  },
  requirementText: {
    color: Colors.textPrimary,
    marginLeft: 10,
    fontSize: 14,
  },
  footer: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    padding: 24,
    paddingBottom: Platform.OS === 'ios' ? 40 : 24,
    flexDirection: 'row',
    backgroundColor: 'rgba(7, 11, 20, 0.8)',
  },
  backButton: {
    width: 56,
    height: 56,
    borderRadius: 16,
    backgroundColor: Colors.surfaceElevated,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 16,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
  },
  applyButtonContainer: {
    flex: 1,
  },
  applyButton: {
    height: 56,
    borderRadius: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    shadowRadius: 8,
    elevation: 5,
  },
  applyText: {
    color: Colors.textPrimary,
    fontSize: 18,
    fontWeight: 'bold',
  },
});
