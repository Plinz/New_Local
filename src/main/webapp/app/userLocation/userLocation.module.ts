import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UserNewLocationComponent } from './userLocation-update.component';
import { NewLocalSharedModule } from '../shared';
import { NewLocalAdminModule } from '../admin/admin.module';
import { userLocationRoute } from '../userLocation/userLocation.route';
import { UserLocationComponent } from './userLocation.component';

const ENTITY_STATES = [...userLocationRoute];
@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [UserLocationComponent, UserNewLocationComponent],
    entryComponents: [UserLocationComponent, UserNewLocationComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalUserLocationModule {}
