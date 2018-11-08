/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { HoldingDetailComponent } from 'app/entities/holding/holding-detail.component';
import { Holding } from 'app/shared/model/holding.model';

describe('Component Tests', () => {
    describe('Holding Management Detail Component', () => {
        let comp: HoldingDetailComponent;
        let fixture: ComponentFixture<HoldingDetailComponent>;
        const route = ({ data: of({ holding: new Holding(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [HoldingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HoldingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HoldingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.holding).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
