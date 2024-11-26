<template>
    <div>
        <h1>팔로우 리스트</h1>
        <p>User ID: {{ userId }}</p> 
    </div>
</template>

<script setup>
import { ref, watch} from "vue";
import { useUserStore } from "@/stores/user";
import { useFollowStore } from "@/stores/follow";
import { useRoute } from "vue-router";
import router from "@/router";

const userStore = useUserStore();
const followStore = useFollowStore();
const route = useRoute();

const userId = ref(route.params.userId);
watch(
  () => route.params.userId,
  (userId) => {
    followStore.getFollowStatus();
  },
  { immediate: true }
);

</script>

<style scoped>

</style>