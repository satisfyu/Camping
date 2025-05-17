package net.satisfy.camping.core.world.entity;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.satisfy.camping.core.registry.CampingEntities;
import net.satisfy.camping.core.registry.CampingSounds;
import net.satisfy.camping.core.world.item.MosquitoRepellentItem;

public class Mosquito extends FlyingMob implements Enemy {

    public enum AttackPhase {
        CIRCLE,
        SWOOP;
    }

    public Vec3 moveTargetPoint = Vec3.ZERO;
    public BlockPos anchorPoint = BlockPos.ZERO;
    public AttackPhase attackPhase = AttackPhase.CIRCLE;

    public int spawnedChildren = 0;
    public boolean fromParent = false;
    public HashMap<UUID, Mosquito> CHILDREN = new HashMap<>();

    public Mosquito(Level level) {
        this(CampingEntities.MOSQUITO, level);
    }

    public Mosquito(EntityType<? extends Mosquito> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 1;
        this.moveControl = new MosquitoMoveControl(this);
        this.lookControl = new MosquitoLookControl(this);
    }

    protected BodyRotationControl createBodyControl() {
        return new MosquitoBodyRotationControl(this);
    }

    public void setSpawnedFromParent(boolean value) {
        this.fromParent = value;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MosquitoAttackStrategyGoal());
        this.goalSelector.addGoal(2, new MosquitoSweepAttackGoalMosquito());
        this.goalSelector.addGoal(3, new MosquitoCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new MosquitoAttackPlayerTargetGoal());
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.35F;
    }

    // make this a config option?
//    protected boolean shouldDespawnInPeaceful() {
//        return true;
//    }

    public void tick() {
        super.tick();

        // create particles on the client
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }

        // update children logic on the server
        else {
            if (!fromParent && spawnedChildren < 2 && CHILDREN.size() < 2) {
                for (int newChild = 0; newChild < 2; newChild++) {
                    Mosquito mosquito = new Mosquito(this.level());
                    mosquito.setSpawnedFromParent(true);
                    mosquito.moveTo(this.position());
                    CHILDREN.put(mosquito.uuid, mosquito);
                    this.level().addFreshEntity(mosquito);
                    spawnedChildren++;
                }
            }
            else {
                if (!CHILDREN.isEmpty()) CHILDREN.entrySet().removeIf(entry -> entry.getValue().isDeadOrDying());
            }
        }
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
        this.anchorPoint = this.blockPosition().above(5);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("AX")) {
            this.anchorPoint = new BlockPos(pCompound.getInt("AX"), pCompound.getInt("AY"), pCompound.getInt("AZ"));
        }

        this.fromParent = pCompound.getBoolean("FromParent");
        this.spawnedChildren = pCompound.getInt("SpawnedChildren");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("AX", this.anchorPoint.getX());
        pCompound.putInt("AY", this.anchorPoint.getY());
        pCompound.putInt("AZ", this.anchorPoint.getZ());
        pCompound.putBoolean("FromParent", this.fromParent);
        pCompound.putInt("SpawnedChildren", this.spawnedChildren);
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true; // if this is false, we can't see hitboxes
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return CampingSounds.MOSQUITO_BUZZ;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return CampingSounds.MOSQUITO_BUZZ;
    }

    protected SoundEvent getDeathSound() {
        return CampingSounds.MOSQUITO_BUZZ;
    }

    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    public boolean canAttackType(EntityType<?> pType) {
        return true;
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.fixed(4.0f, 4.0f); // so the players can swat the mosquito easily
    }

    public double getPassengersRidingOffset() {
        return this.getEyeHeight();
    }

    static class MosquitoLookControl extends LookControl {

        public MosquitoLookControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {}
    }

    class MosquitoAttackPlayerTargetGoal extends Goal {

        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            else {

                this.nextScanTick = reducedTickDelay(60);
                List<Player> list = Mosquito.this.level().getNearbyPlayers(this.attackTargeting, Mosquito.this, Mosquito.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));

                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(Player player : list) {
                        if (Mosquito.this.canAttack(player, TargetingConditions.DEFAULT)) {
                            Mosquito.this.setTarget(player);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = Mosquito.this.getTarget();
            return livingentity != null && Mosquito.this.canAttack(livingentity, TargetingConditions.DEFAULT);
        }
    }

    class MosquitoAttackStrategyGoal extends Goal {

        private int nextSweepTick;

        public boolean canUse() {
            LivingEntity livingentity = Mosquito.this.getTarget();
            return livingentity != null ? Mosquito.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            Mosquito.this.attackPhase = Mosquito.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            Mosquito.this.anchorPoint = Mosquito.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Mosquito.this.anchorPoint).above(5 + Mosquito.this.random.nextInt(5));
        }

        public void tick() {
            if (Mosquito.this.attackPhase == Mosquito.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    Mosquito.this.attackPhase = Mosquito.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + Mosquito.this.random.nextInt(4)) * 20);
                    Mosquito.this.playSound(CampingSounds.MOSQUITO_BUZZ, 10.0F, 0.95F + Mosquito.this.random.nextFloat() * 0.1F);
                }
            }
        }

        private void setAnchorAboveTarget() {
            Mosquito.this.anchorPoint = Mosquito.this.getTarget().blockPosition().above(20 + Mosquito.this.random.nextInt(20));
            if (Mosquito.this.anchorPoint.getY() < Mosquito.this.level().getSeaLevel()) {
                Mosquito.this.anchorPoint = new BlockPos(Mosquito.this.anchorPoint.getX(), Mosquito.this.level().getSeaLevel() + 1, Mosquito.this.anchorPoint.getZ());
            }
        }
    }

    class MosquitoBodyRotationControl extends BodyRotationControl {

        public MosquitoBodyRotationControl(Mob pMob) {
            super(pMob);
        }

        public void clientTick() {
            Mosquito.this.yHeadRot = Mosquito.this.yBodyRot;
            Mosquito.this.yBodyRot = Mosquito.this.getYRot();
        }
    }

    class MosquitoMoveControl extends MoveControl {

        private float speed = 0.1F;

        public MosquitoMoveControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {

            if (Mosquito.this.horizontalCollision) {
                Mosquito.this.setYRot(Mosquito.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double targetSelfDeltaX = Mosquito.this.moveTargetPoint.x - Mosquito.this.getX();
            double targetSelfDeltaY = Mosquito.this.moveTargetPoint.y - Mosquito.this.getY();
            double targetSelfDeltaZ = Mosquito.this.moveTargetPoint.z - Mosquito.this.getZ();
            double deltaSqrt = Math.sqrt(targetSelfDeltaX * targetSelfDeltaX + targetSelfDeltaZ * targetSelfDeltaZ);
            if (Math.abs(deltaSqrt) > (double)1.0E-5F) {
                double horizontalMod = 1.0D - Math.abs(targetSelfDeltaY * (double)0.7F) / deltaSqrt;
                targetSelfDeltaX *= horizontalMod;
                targetSelfDeltaZ *= horizontalMod;
                deltaSqrt = Math.sqrt(targetSelfDeltaX * targetSelfDeltaX + targetSelfDeltaZ * targetSelfDeltaZ);
                double deltaSqrSqrt = Math.sqrt(targetSelfDeltaX * targetSelfDeltaX + targetSelfDeltaZ * targetSelfDeltaZ + targetSelfDeltaY * targetSelfDeltaY);
                float yRot = Mosquito.this.getYRot();
                float xz_atan = (float)Mth.atan2(targetSelfDeltaZ, targetSelfDeltaX);
                float verticalDegree = Mth.wrapDegrees(Mosquito.this.getYRot() + 90.0F);
                float horizontalDegree = Mth.wrapDegrees(xz_atan * (180F / (float)Math.PI));
                Mosquito.this.setYRot(Mth.approachDegrees(verticalDegree, horizontalDegree, 4.0F) - 90.0F);
                Mosquito.this.yBodyRot = Mosquito.this.getYRot();
                if (Mth.degreesDifferenceAbs(yRot, Mosquito.this.getYRot()) < 3.0F) {
                     // this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                     this.speed = Mth.approach(this.speed, 1F, 0.005F * (1F / this.speed));
                }
                else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float ysqrt_atan = (float)(-(Mth.atan2(-targetSelfDeltaY, deltaSqrt) * (double)(180F / (float)Math.PI)));
                Mosquito.this.setXRot(ysqrt_atan);
                float verticalRightAngle = Mosquito.this.getYRot() + 90.0F;
                double xSpeed = (double)(this.speed * Mth.cos(verticalRightAngle * ((float)Math.PI / 180F))) * Math.abs(targetSelfDeltaX / deltaSqrSqrt);
                double ySpeed = (double)(this.speed * Mth.sin(verticalRightAngle * ((float)Math.PI / 180F))) * Math.abs(targetSelfDeltaZ / deltaSqrSqrt);
                double zSpeed = (double)(this.speed * Mth.sin(ysqrt_atan * ((float)Math.PI / 180F))) * Math.abs(targetSelfDeltaY / deltaSqrSqrt);
                Vec3 vec3 = Mosquito.this.getDeltaMovement();
                Mosquito.this.setDeltaMovement(vec3.add((new Vec3(xSpeed, zSpeed, ySpeed)).subtract(vec3).scale(0.2D)));
            }

        }
    }

    abstract class MosquitoMoveTargetGoal extends Goal {
        public MosquitoMoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return Mosquito.this.moveTargetPoint.distanceToSqr(Mosquito.this.getX(), Mosquito.this.getY(), Mosquito.this.getZ()) < 4.0D;
        }
    }

    class MosquitoCircleAroundAnchorGoal extends MosquitoMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        public boolean canUse() {
            return Mosquito.this.getTarget() == null || Mosquito.this.attackPhase == Mosquito.AttackPhase.CIRCLE;
        }

        public void start() {
            // this.distance = 5.0F + Mosquito.this.random.nextFloat() * 10.0F;
            this.distance = 5.0F + Mosquito.this.random.nextFloat() * 5.0F;
            this.height = -4.0F + Mosquito.this.random.nextFloat() * 9.0F;
            this.clockwise = Mosquito.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (Mosquito.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + Mosquito.this.random.nextFloat() * 9.0F;
            }

            if (Mosquito.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (Mosquito.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = Mosquito.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (Mosquito.this.moveTargetPoint.y < Mosquito.this.getY() && !Mosquito.this.level().isEmptyBlock(Mosquito.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (Mosquito.this.moveTargetPoint.y > Mosquito.this.getY() && !Mosquito.this.level().isEmptyBlock(Mosquito.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(Mosquito.this.anchorPoint)) {
                Mosquito.this.anchorPoint = Mosquito.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            Mosquito.this.moveTargetPoint = Vec3.atLowerCornerOf(Mosquito.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class MosquitoSweepAttackGoalMosquito extends MosquitoMoveTargetGoal {

        private static final int REPEL_SEARCH_TICK_DELAY = 20;

        private int repelSearchTick = 0;
        private boolean repelled = false;

        public boolean canUse() {
            return Mosquito.this.getTarget() != null && Mosquito.this.attackPhase == Mosquito.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = Mosquito.this.getTarget();
            if (livingentity == null) {
                return false;
            }
            else if (!livingentity.isAlive()) {
                return false;
            }
            else {

                if (livingentity instanceof Player player) {
                    if (livingentity.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }

                if (!this.canUse()) {
                    return false;
                }
                else {
                    if (Mosquito.this.tickCount > this.repelSearchTick) {
                        this.repelSearchTick = Mosquito.this.tickCount + REPEL_SEARCH_TICK_DELAY;

                        List<Player> players = Mosquito.this.level().getEntitiesOfClass(Player.class, Mosquito.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);

                        for (Player player : players) {
                            if (player.getTags().contains(MosquitoRepellentItem.PLAYER_TAG)) this.repelled = true;
                        }
                    }
                }

                return !this.repelled;
            }
        }

        public void start() {
        }

        public void stop() {
            Mosquito.this.setTarget((LivingEntity)null);
            Mosquito.this.attackPhase = Mosquito.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity livingentity = Mosquito.this.getTarget();
            if (livingentity != null) {
                Mosquito.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.75D), livingentity.getZ());
                if (Mosquito.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
                    Mosquito.this.doHurtTarget(livingentity);
                    Mosquito.this.attackPhase = Mosquito.AttackPhase.CIRCLE;
                }
                else if (Mosquito.this.horizontalCollision || Mosquito.this.hurtTime > 0) {
                    Mosquito.this.attackPhase = Mosquito.AttackPhase.CIRCLE;
                }

            }
        }
    }
}