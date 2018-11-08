import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { MainSearchComponent, MainSearchDetailComponent, mainSearchRoute } from './';

const ENTITY_STATES = [...mainSearchRoute];

@NgModule({
    imports: [NewLocalSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MainSearchComponent, MainSearchDetailComponent],
    entryComponents: [MainSearchComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalMainSearchModule {}
