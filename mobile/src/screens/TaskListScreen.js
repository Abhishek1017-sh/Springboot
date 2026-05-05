import React, { useState, useEffect } from 'react';
import { 
  StyleSheet, 
  Text, 
  View, 
  FlatList, 
  TouchableOpacity, 
  ActivityIndicator, 
  Dimensions,
  Animated as RNAnimated
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { BlurView } from 'expo-blur';
import { Colors } from '../theme/colors';
import apiClient from '../api/apiClient';
import { Ionicons } from '@expo/vector-icons';

const { width } = Dimensions.get('window');

const TaskCard = ({ item, index, onPress }) => {
  const [fadeAnim] = useState(new RNAnimated.Value(0));
  const [slideAnim] = useState(new RNAnimated.Value(30));

  useEffect(() => {
    RNAnimated.parallel([
      RNAnimated.timing(fadeAnim, {
        toValue: 1,
        duration: 600,
        delay: index * 100,
        useNativeDriver: true,
      }),
      RNAnimated.timing(slideAnim, {
        toValue: 0,
        duration: 600,
        delay: index * 100,
        useNativeDriver: true,
      })
    ]).start();
  }, []);

  return (
    <RNAnimated.View style={{ opacity: fadeAnim, transform: [{ translateY: slideAnim }] }}>
      <TouchableOpacity 
        activeOpacity={0.9}
        onPress={onPress}
      >
        <BlurView intensity={15} tint="dark" style={styles.taskCard}>
          <View style={styles.cardHeader}>
            <View style={styles.iconContainer}>
              <Ionicons name="calendar-outline" size={20} color={Colors.accent} />
            </View>
            <View style={styles.tagContainer}>
              <Text style={styles.tagText}>{item.date}</Text>
            </View>
          </View>
          
          <Text style={styles.taskTitle}>{item.title}</Text>
          <View style={styles.locationContainer}>
            <Ionicons name="location-outline" size={16} color={Colors.textSecondary} />
            <Text style={styles.taskDetail}>{item.location}</Text>
          </View>

          <View style={styles.cardFooter}>
            <View style={styles.skillsList}>
              {item.skillsRequired && item.skillsRequired.slice(0, 2).map((skill, i) => (
                <View key={i} style={styles.skillBadge}>
                  <Text style={styles.skillText}>{skill}</Text>
                </View>
              ))}
            </View>
            <Ionicons name="chevron-forward" size={20} color={Colors.accent} />
          </View>
        </BlurView>
      </TouchableOpacity>
    </RNAnimated.View>
  );
};

export default function TaskListScreen({ navigation }) {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await apiClient.get('/tasks');
      setTasks(response.data);
    } catch (error) {
      console.error('Error fetching tasks:', error);
      setTasks([
        { id: 1, title: 'Community Kitchen Help', location: 'Downtown Center', date: '2026-05-10', skillsRequired: ['Cooking', 'Teamwork'] },
        { id: 2, title: 'Park Clean-up', location: 'Central Park', date: '2026-05-12', skillsRequired: ['Manual Labor', 'Environment'] },
        { id: 3, title: 'Senior Care Visit', location: 'Sunset Home', date: '2026-05-15', skillsRequired: ['Empathy', 'Social'] },
      ]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color={Colors.accent} />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <LinearGradient colors={Colors.gradientHero} style={StyleSheet.absoluteFill} />
      
      <View style={styles.header}>
        <View>
          <Text style={styles.welcome}>Impact Hub</Text>
          <Text style={styles.headerTitle}>Active Tasks</Text>
        </View>
        <TouchableOpacity style={styles.profileBtn}>
          <Ionicons name="person-circle-outline" size={36} color={Colors.accent} />
        </TouchableOpacity>
      </View>

      <FlatList
        data={tasks}
        renderItem={({ item, index }) => (
          <TaskCard 
            item={item} 
            index={index} 
            onPress={() => navigation.navigate('TaskDetail', { task: item })} 
          />
        )}
        keyExtractor={item => item.id.toString()}
        contentContainerStyle={styles.list}
        showsVerticalScrollIndicator={false}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  header: {
    paddingTop: 60,
    paddingHorizontal: 24,
    paddingBottom: 24,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  welcome: {
    fontSize: 14,
    color: Colors.accent,
    fontWeight: '600',
    letterSpacing: 2,
    textTransform: 'uppercase',
  },
  headerTitle: {
    fontSize: 32,
    fontWeight: 'bold',
    color: Colors.textPrimary,
    letterSpacing: -0.5,
  },
  list: {
    padding: 20,
    paddingBottom: 100,
  },
  taskCard: {
    padding: 20,
    borderRadius: 24,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
    overflow: 'hidden',
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
  },
  iconContainer: {
    width: 36,
    height: 36,
    borderRadius: 10,
    backgroundColor: 'rgba(0, 212, 255, 0.1)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  tagContainer: {
    backgroundColor: 'rgba(255, 255, 255, 0.05)',
    paddingHorizontal: 12,
    paddingVertical: 4,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: Colors.glassBorder,
  },
  tagText: {
    color: Colors.textSecondary,
    fontSize: 12,
    fontWeight: '600',
  },
  taskTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: Colors.textPrimary,
    marginBottom: 8,
  },
  locationContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  taskDetail: {
    fontSize: 14,
    color: Colors.textSecondary,
    marginLeft: 4,
  },
  cardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  skillsList: {
    flexDirection: 'row',
  },
  skillBadge: {
    backgroundColor: 'rgba(108, 99, 255, 0.15)',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 8,
    marginRight: 8,
  },
  skillText: {
    color: Colors.primaryLight,
    fontSize: 11,
    fontWeight: '700',
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: Colors.background,
  },
});
