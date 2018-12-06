import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from '../shared';
import { NewLocalAdminModule } from '../admin/admin.module';

import {
    HoldingProfilComponent,
    HoldingProfilDetailComponent,
    HoldingProfilUpdateComponent,
    HoldingProfilDeletePopupComponent,
    HoldingProfilDeleteDialogComponent,
    holdingProfilRoute,
    holdingProfilPopupRoute
} from './';

const ENTITY_STATES = [...holdingProfilRoute, ...holdingProfilPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HoldingProfilComponent,
        HoldingProfilDetailComponent,
        HoldingProfilUpdateComponent,
        HoldingProfilDeleteDialogComponent,
        HoldingProfilDeletePopupComponent
    ],
    entryComponents: [
        HoldingProfilComponent,
        HoldingProfilUpdateComponent,
        HoldingProfilDeleteDialogComponent,
        HoldingProfilDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalProfilHoldingModule {}
